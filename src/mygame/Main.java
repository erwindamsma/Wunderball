package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
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
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.util.ArrayList;
import java.util.List;

public class Main extends SimpleApplication implements ActionListener {
    
    public static final Quaternion PITCH270 = new Quaternion().fromAngleAxis(FastMath.PI*3/2, new Vector3f(1,0,0));
    public static final Quaternion YAW270   = new Quaternion().fromAngleAxis(FastMath.PI*3/2, new Vector3f(0,1,0));
    
    private BulletAppState bulletAppState;
    private Nifty nifty;
    private BitmapText hudText;
    private BitmapText debugText; //for xyz coords of the ball
    private ChaseCamera chaseCam;
    
    private Ball ball;
    private RigidBodyControl rigidBall;
    
    private List<Coin> coins = new ArrayList<>();
    
    private boolean rightBtn = false;
    private boolean leftBtn = false;
    private boolean upBtn = false;
    private boolean downBtn = false;
    
    private Player player;
    private boolean isRunning = false;
    private static final int ballSpeed = 10;
    
    private List<Vector3f> coinLocations = new ArrayList<Vector3f>(){{
        add(new Vector3f(0.8f, 0.2f, 0.8f));
        add(new Vector3f(-0.8f, 0.2f, 0.8f));
        add(new Vector3f(0.8f, 0.2f, -0.9f));
        add(new Vector3f(-0.8f, 0.2f, -0.9f));
        add(new Vector3f(0.167f, 0.25f, -5.9f));
        add(new Vector3f(3.3f, -0.12f, -7.2f));
    }};
    
    public static void main(String[] args) {
        Main app = new Main();
        AppSettings s = new AppSettings(true);
        s.setFullscreen(false);
        s.setWidth(1366);
        s.setHeight(768);
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
        flyCam.setEnabled(false);
        player = new Player("", this);
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/screen.xml", "start", new MyStartScreen(this, nifty));
        guiViewPort.addProcessor(niftyDisplay);
        
        Spatial parcour = assetManager.loadModel("Models/parcour/parcour.j3o");
        parcour.setLocalRotation(YAW270);
        RigidBodyControl rigidParcour = new RigidBodyControl(0);
        parcour.addControl(rigidParcour);
        rigidParcour.setRestitution(1);
        rootNode.attachChild(parcour);
        bulletAppState.getPhysicsSpace().add(rigidParcour);
        
        rigidBall = new RigidBodyControl(new SphereCollisionShape(0.3f));
        rigidBall.setDamping(0.5f, 0.5f);
        ball = new Ball(assetManager, this);
        ball.setLocalTranslation(0, 1, 0);
        ball.addControl(rigidBall);

        initLight();
        initSkySphere();
        initChaseCam();
        initCoins();
        initDebugText(); //for xyz coords of the ball
    }

    @Override
    public void simpleUpdate(float tpf) {
        double rotation = this.chaseCam.getHorizontalRotation();
        
        float x = (float)(Math.cos(rotation) * ballSpeed);
        float y = (float)(Math.sin(rotation) * ballSpeed);
        
        if (rightBtn) rigidBall.applyCentralForce(new Vector3f(y,0,-x));
        if (leftBtn) rigidBall.applyCentralForce(new Vector3f(-y,0,x));
        if (upBtn) rigidBall.applyCentralForce(new Vector3f(-x,0,-y));
        if (downBtn) rigidBall.applyCentralForce(new Vector3f(x,0,y));
        
        checkUserDropped();
        checkFinished();
        checkCoins(tpf);
        updateDebugText(); //for xyz coords of the ball
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void initLight(){
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
    
    private void initSkySphere() {
        Sphere skySphere = new Sphere(10, 10, 200, false, true);
        Geometry skySphereGeom = new Geometry("skyDome", skySphere);
        Material skySphereMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        skySphereMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Sky_horiz_6-2048.jpg"));
        skySphereMat.setColor("Diffuse", ColorRGBA.White);
        skySphereGeom.setMaterial(skySphereMat);
        skySphereGeom.setLocalTranslation(0, 0, 0);
        skySphereGeom.setLocalRotation(PITCH270);
        rootNode.attachChild(skySphereGeom);
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
            case "Jump":
                if (isPressed) rigidBall.applyCentralForce(new Vector3f(0,300,0));
                break;
            
            case "1":
                ball.switchBall(BallChoice.Bowlingball);
                break;
            case "2":
                ball.switchBall(BallChoice.Basketball);
                break;
            case "3":
                ball.switchBall(BallChoice.Baseball);
                break;
            case "4":
                ball.switchBall(BallChoice.Tennisball);
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
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Jump");
        
        inputManager.addMapping("1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addListener(this, "1");
        inputManager.addMapping("2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addListener(this, "2");
        inputManager.addMapping("3", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addListener(this, "3");
        inputManager.addMapping("4", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addListener(this, "4");
    }
    
    private void checkUserDropped(){
        if (rigidBall.getPhysicsLocation().y < -20){
            rigidBall.setLinearVelocity(new Vector3f(0,0,0));
            rigidBall.setAngularVelocity(new Vector3f(0,0,0));
            rigidBall.setPhysicsLocation(new Vector3f(0, 1, 0));
            player.addRespawn();
        }
    }
    
    private void initHudText(){
        hudText = new BitmapText(guiFont);          
        hudText.setSize(guiFont.getCharSet().getRenderedSize());
        hudText.setColor(ColorRGBA.Blue);
        hudText.setLocalTranslation(5, cam.getHeight() - 5, 0);
        guiNode.attachChild(hudText);
        
        updateHudText();
    }
    
    public void updateHudText(){
        hudText.setText("Player: " + player.getPlayerName() + "\nScore: " + player.getScore());
    }
    
    private void initDebugText(){
        debugText = new BitmapText(guiFont);
        debugText.setSize(guiFont.getCharSet().getRenderedSize());
        debugText.setColor(ColorRGBA.Blue);
        debugText.setLocalTranslation(5, cam.getHeight() - 50, 0);
        guiNode.attachChild(debugText);
    }
    
    private void updateDebugText(){
        String debugTextString = "X: ";
        debugTextString += String.valueOf(rigidBall.getPhysicsLocation().getX());
        debugTextString += "\nY: ";
        debugTextString += String.valueOf(rigidBall.getPhysicsLocation().getY());
        debugTextString += "\nZ: ";
        debugTextString += String.valueOf(rigidBall.getPhysicsLocation().getZ());
        debugText.setText(debugTextString);
    }
    
    private void initChaseCam(){
        chaseCam = new ChaseCamera(cam, ball, this.inputManager);
        chaseCam.setDefaultDistance(5);
        chaseCam.setMinDistance(3);
        chaseCam.setMaxDistance(10);
        chaseCam.setDefaultHorizontalRotation((float)(90 * Math.PI / 180));
        chaseCam.setInvertVerticalAxis(true);
    }
    
    private void checkFinished(){
        if (isRunning){
            float distance = ball.getLocalTranslation().distance(new Vector3f(3.5f, -2.76f, -5.9f));
            if (distance < ((SphereCollisionShape)rigidBall.getCollisionShape()).getRadius() + 0.5f){
                isRunning = false;
                rootNode.detachChild(ball);
                new LeaderboardHandler().addScore(player.getPlayerName(), player.getScore());
                nifty.fromXml("Interface/screen.xml", "end", new MyEndScreen(nifty));
                
            }
        }
    }
    
    private void initCoins(){
        for (Vector3f v : coinLocations){
            Coin coin = new Coin(assetManager);
            coin.setLocalTranslation(v);
            rootNode.attachChild(coin);
            coins.add(coin);
        }
    }
    
    private void checkCoins(float tpf){
        for (int i = 0; i < coins.size(); i++){
            coins.get(i).rotate(0, tpf, 0);
            float distance = ball.getLocalTranslation().distance(coins.get(i).getLocalTranslation());
            if (distance < ((SphereCollisionShape)rigidBall.getCollisionShape()).getRadius() + ((Cylinder)((Geometry)coins.get(i).getChild(0)).getMesh()).getRadius() - 0.01f){
                rootNode.detachChild(coins.get(i));
                coins.remove(i);
                player.addPoint();
            }
        }
    }
    
    public void activateGame(String name){
        isRunning = true;
        player.setPlayerName(name);
        initHudText();
        rootNode.attachChild(ball);
        bulletAppState.getPhysicsSpace().add(rigidBall);
        chaseCam.setDragToRotate(false);
        initKeys();
    }
    
    public void updateRigidBall(float r, float mass, float bouncyness){
        rigidBall.setPhysicsLocation(rigidBall.getPhysicsLocation().add(0, r - ((SphereCollisionShape)rigidBall.getCollisionShape()).getRadius() + 0.05f, 0));
        rigidBall.setMass(mass);
        rigidBall.setRestitution(bouncyness);
        rigidBall.setCollisionShape(new SphereCollisionShape(r));
    }
}