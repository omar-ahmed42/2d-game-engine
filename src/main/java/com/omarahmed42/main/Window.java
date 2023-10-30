package com.omarahmed42.main;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import com.omarahmed42.observers.EventSystem;
import com.omarahmed42.observers.Observer;
import com.omarahmed42.observers.events.Event;
import com.omarahmed42.renderer.DebugDraw;
import com.omarahmed42.renderer.FrameBuffer;
import com.omarahmed42.renderer.PickingTexture;
import com.omarahmed42.renderer.Renderer;
import com.omarahmed42.renderer.Shader;
import com.omarahmed42.scenes.LevelEditorSceneInitializer;
import com.omarahmed42.scenes.Scene;
import com.omarahmed42.scenes.SceneInitializer;
import com.omarahmed42.util.AssetPool;

import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;

public class Window implements Observer {
    private int width;
    private int height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imGuiLayer;
    private FrameBuffer frameBuffer;
    private PickingTexture pickingTexture;

    private static Window window = null;

    private static Scene currentScene;
    private boolean runtimePlaying = false;

    private Window() {
        this.width = 1920;
        this.height = 1017;
        this.title = "Main";
        EventSystem.addObserver(this);
    }

    public static void changeScene(SceneInitializer sceneInitializer) {
        if (currentScene != null) {
            currentScene.destroy();
        }
        
        getImGuiLayer().getPropertiesWindow().setActiveGameObject(null);;
        currentScene = new Scene(sceneInitializer);
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public static Window get() {
        if (Window.window == null)
            Window.window = new Window();

        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (glfwWindow == NULL)
            throw new IllegalStateException("Failed to create the GLFW window");

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        
        this.frameBuffer = new FrameBuffer(1920, 1017);
        this.pickingTexture = new PickingTexture(1920, 1017);
        
        this.imGuiLayer = new ImGuiLayer(glfwWindow, this.pickingTexture);
        this.imGuiLayer.initImGui();

        glViewport(0, 0, 1920, 1017);
        Window.changeScene(new LevelEditorSceneInitializer());
    }

    public void loop() {
        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            // Render pass 1. Render to picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();
            
            glViewport(0, 0, 1920, 1017);
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            Renderer.bindShader(pickingShader);
            currentScene.render();

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);
            // Render pass 2. Render actual game
            DebugDraw.beginFrame();

            this.frameBuffer.bind();
            glClearColor(1, 1, 1, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0){
                DebugDraw.draw();
                Renderer.bindShader(defaultShader);
                if (runtimePlaying) {
                    currentScene.update(dt);
                } else {
                    currentScene.editorUpdate(dt);
                }
                currentScene.render();
            }
            this.frameBuffer.unbind();

            this.imGuiLayer.update(dt, currentScene);
            glfwSwapBuffers(glfwWindow);
            MouseListener.endFrame();

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        currentScene.save();
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int width) {
        get().width = width;
    }

    public static void setHeight(int height) {
        get().height = height;
    }

    public static FrameBuffer getFramebuffer() {
        return get().frameBuffer;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }

    public static ImGuiLayer getImGuiLayer() {
        return get().imGuiLayer;
    }

    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type) {
            case GameEngineStartPlay:
                this.runtimePlaying = true;
                currentScene.save();
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case GameEngineStopPlay:
                this.runtimePlaying = false;
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case LoadLevel:
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case SaveLevel:
                currentScene.save();
                break;
        }
    }

    
}
