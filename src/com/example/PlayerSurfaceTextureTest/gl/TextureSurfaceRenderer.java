package com.example.PlayerSurfaceTextureTest.gl;


import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import javax.microedition.khronos.egl.*;


public abstract class TextureSurfaceRenderer implements Runnable
{
    private static final int EGL_OPENGL_ES2_BIT = 4;
    private static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
    private static final String LOG_TAG = "SurfaceTest.GL";
    protected SurfaceTexture texture;
    private EGL10 egl;
    private EGLDisplay eglDisplay;
    private EGLConfig eglConfig;
    private EGLContext eglContext;
    private EGLSurface eglSurface;

    protected int width;
    protected int height;
    private boolean running;

    public TextureSurfaceRenderer(SurfaceTexture texture, int width, int height)
    {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.running = true;
        Thread thrd = new Thread(this);
        thrd.start();
    }

    @Override
    public void run()
    {
        initGL();
        initGLComponents();
        Log.d(LOG_TAG, "OpenGL init OK.");

        while (running)
        {
            pingFps();
            draw();
            egl.eglSwapBuffers(eglDisplay, eglSurface);
        }

        deinitGL();
    }

    protected abstract void draw();
    protected abstract void initGLComponents();

    private long lastFpsOutput = 0;
    private int frames;
    private void pingFps()
    {
        if (lastFpsOutput == 0)
            lastFpsOutput = System.currentTimeMillis();

        frames ++;

        if (System.currentTimeMillis() - lastFpsOutput > 1000)
        {
            Log.d(LOG_TAG, "FPS: " + frames);
            lastFpsOutput = System.currentTimeMillis();
            frames = 0;
        }
    }

    public void onPause()
    {
        running = false;
    }

    private void initGL()
    {
        egl = (EGL10) EGLContext.getEGL();
        eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        
        int[] version = new int[2];
        egl.eglInitialize(eglDisplay, version);
        
        eglConfig = chooseEglConfig();
        eglContext = createContext(egl, eglDisplay, eglConfig);

        eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, texture, null);

        if (eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE)
        {
            throw new RuntimeException("GL Error: " + GLUtils.getEGLErrorString(egl.eglGetError()));
        }

        if (!egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext))
        {
            throw new RuntimeException("GL Make current error: " + GLUtils.getEGLErrorString(egl.eglGetError()));
        }
    }

    private void deinitGL()
    {
        egl.eglDestroyContext(eglDisplay, eglContext);
        egl.eglDestroySurface(eglDisplay, eglSurface);
        Log.d(LOG_TAG, "OpenGL deinit OK.");
    }

    private EGLContext createContext(EGL10 egl, EGLDisplay eglDisplay, EGLConfig eglConfig)
    {
        int[] attribList = { EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE };
        return egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attribList);
    }

    private EGLConfig chooseEglConfig()
    {
        int[] configsCount = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        int[] configSpec = getConfig();

        if (!egl.eglChooseConfig(eglDisplay, configSpec, configs, 1, configsCount))
        {
            throw new IllegalArgumentException("Failed to choose config: " + GLUtils.getEGLErrorString(egl.eglGetError()));
        }
        else if (configsCount[0] > 0)
        {
            return configs[0];
        }

        return null;
    }

    private int[] getConfig()
    {
        return new int[] {
                EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 16,
                EGL10.EGL_STENCIL_SIZE, 0,
                EGL10.EGL_NONE
        };
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        running = false;
    }
}
