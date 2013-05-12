package com.example.PlayerSurfaceTextureTest;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;
import com.example.PlayerSurfaceTextureTest.gl.TextureSurfaceRenderer;
import com.example.PlayerSurfaceTextureTest.gl.VideoTextureRenderer;

public class SurfaceActivity extends Activity implements TextureView.SurfaceTextureListener
{
    private static final String LOG_TAG = "SurfaceTest";

    private TextureView surface;
    private TextureSurfaceRenderer renderer;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        surface = (TextureView) findViewById(R.id.surface);
        surface.setSurfaceTextureListener(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (renderer != null)
            renderer.onPause();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
    {
        renderer = new VideoTextureRenderer(this, surface, width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
