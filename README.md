TextureView Video playback demo
================================

This is a demo for using OpenGL to render video to a TextureView.

With this you can use OpenGL shaders for video effects during playback.

Important files:

`TextureSurfaceRenderer.java` - Similar to GLSurfaceView, it initialiazes OpenGL context on a TextureView surface and has an abstract `draw()` function where OpenGL drawing should be done.

`VideoTextureRendrer.java` - Extension of TextureSurfaceRendrer that uses OpenGL shaders to render video with OpenGL.

