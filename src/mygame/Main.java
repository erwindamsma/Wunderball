package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {
    
    public static final Quaternion PITCH270 = new Quaternion().fromAngleAxis(FastMath.PI*3/2, new Vector3f(1,0,0));
    private BulletAppState bulletAppState;
    
    BitmapText hudText;
    
    private ChaseCamera chaseCam;
    
    Ball ball;
    RigidBodyControl rigidBall;
    
    private boolean rightBtn = false;
    private boolean leftBtn = false;
    private boolean upBtn = false;
    private boolean downBtn = false;
    
    private int failCounter = 0;
    
    public static void main(String[] args) {
        Main app = new Main();
        AppSettings s = new AppSettings(true);
        s.setFullscreen(true);
        s.setWidth(1920);
        s.setHeight(1080);
        s.setFrameRate(60);
        s.setSamples(2); //antialiasing
        app.setSettings(s);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        setUpLight();
        setUpSkyDome();
        
        Node parcour = new Parcour(assetManager);
        RigidBodyControl rigidParcour = new RigidBodyControl(0);
        parcour.addControl(rigidParcour);
        rootNode.attachChild(parcour);
        bulletAppState.getPhysicsSpace().add(rigidParcour);
        
        ball = new Ball(assetManager, BallChoice.Basketball);
        ball.setLocalTranslation(0, 2, 0);
        rigidBall = new RigidBodyControl(5);
        ball.addControl(rigidBall);
        rootNode.attachChild(ball);
        bulletAppState.getPhysicsSpace().add(rigidBall);
        
        flyCam.setEnabled(false);
        
        chaseCam = new ChaseCamera(cam, ball, this.inputManager);
        chaseCam.setDefaultDistance(5);
        chaseCam.setMinDistance(3);
        chaseCam.setMaxDistance(10);
        chaseCam.setDefaultHorizontalRotation((float)(90 * Math.PI / 180));
        chaseCam.setDragToRotate(false);
        chaseCam.setInvertVerticalAxis(true);
        
        initKeys();
        
        hudText = new BitmapText(guiFont, false);          
        hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.Blue);                             // font color
        hudText.setText("You can write any string here");             // the text
        hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
        guiNode.attachChild(hudText);
    }

    @Override
    public void simpleUpdate(float tpf) {
        double rotation = (float)(this.chaseCam.getHorizontalRotation() / Math.PI * 180) - 90;
        
        hudText.setText(String.valueOf(Math.abs(rotation % 360)) + " ==> x: " + Math.sin(rotation) * 20 + " z: " + Math.cos(rotation) * 20);
        
        
        
        if(rightBtn){
            rigidBall.applyCentralForce(new Vector3f(20,0,0));
        }
        if (leftBtn){
            rigidBall.applyCentralForce(new Vector3f(-20,0,0));
        }
        if (upBtn){
            rigidBall.applyCentralForce(new Vector3f(0,0,-20));
        }
        if (downBtn){
            rigidBall.applyCentralForce(new Vector3f(0,0,20));
        }
        
        this.inputManager.setCursorVisible(false);
        checkUserDropped();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void setUpLight(){
        DirectionalLight sun1 = new DirectionalLight();
        sun1.setColor(ColorRGBA.White);
        sun1.setDirection(new Vector3f(1,0,0).normalizeLocal());
        rootNode.addLight(sun1);
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setColor(ColorRGBA.White);
        sun2.setDirection(new Vector3f(-1,0,0).normalizeLocal());
        rootNode.addLight(sun2);
        DirectionalLight sun3 = new DirectionalLight();
        sun3.setColor(ColorRGBA.White);
        sun3.setDirection(new Vector3f(0,0,1).normalizeLocal());
        rootNode.addLight(sun3);
        DirectionalLight sun4 = new DirectionalLight();
        sun4.setColor(ColorRGBA.White);
        sun4.setDirection(new Vector3f(0,0,-1).normalizeLocal());
        rootNode.addLight(sun4);
        DirectionalLight sun5 = new DirectionalLight();
        sun5.setColor(ColorRGBA.White);
        sun5.setDirection(new Vector3f(0,1,0).normalizeLocal());
        rootNode.addLight(sun5);
        DirectionalLight sun6 = new DirectionalLight();
        sun6.setColor(ColorRGBA.White);
        sun6.setDirection(new Vector3f(0,-1,0).normalizeLocal());
        rootNode.addLight(sun6);
    }
    
    private void setUpSkyDome() {
        Sphere skySphere = new Sphere(10, 10, 200, false, true);
        Geometry skyDomeGeom = new Geometry("skyDome", skySphere);
        Material skyDomeMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        skyDomeMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Sky_horiz_6-2048.jpg"));
        skyDomeMat.setColor("Diffuse", ColorRGBA.White);
        skyDomeGeom.setMaterial(skyDomeMat);
        skyDomeGeom.setLocalTranslation(0, 0, 0);
        skyDomeGeom.setLocalRotation(PITCH270);
        rootNode.attachChild(skyDomeGeom);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        switch (name) {
            case "Left":
                leftBtn = isPressed;
                break;
            case "Right":
                rightBtn = isPressed;
                break;
            case "Up":
                upBtn = isPressed;
                break;
            case "Down":
                downBtn = isPressed;
                break;
        }
    }
        
    private void initKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addListener(this, "Left");
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(this, "Right");
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addListener(this, "Up");
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(this, "Down");
    }
    
    private void checkUserDropped(){
        if (rigidBall.getPhysicsLocation().y < -20){
            rigidBall.setLinearVelocity(new Vector3f(0,0,0));
            rigidBall.setAngularVelocity(new Vector3f(0,0,0));
            rigidBall.setPhysicsLocation(new Vector3f(0, 1, 0));
            failCounter++;
        }
    }
}
