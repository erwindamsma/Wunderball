package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import static mygame.BallChoice.Baseball;
import static mygame.BallChoice.Basketball;
import static mygame.BallChoice.Bowlingball;
import static mygame.BallChoice.Tennisball;

public class Ball extends Node {
    private final AssetManager assetManager;
    private final Main main;
    
    private Sphere ballSphere;
    private Geometry ballGeom;
    private Texture ballTexture;
    private Material ballMat;
    
    private BallChoice currentBallChoice;
    
    public Ball(AssetManager assetManager, Main main){
        this.assetManager = assetManager;
        this.main = main;
        
        ballSphere = new Sphere();
        ballGeom = new Geometry("ballSphere", ballSphere);
        ballTexture = assetManager.loadTexture("Textures/BasketballColor.jpg");
        ballMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        ballMat.setTexture("DiffuseMap", ballTexture);
        ballMat.setColor("Diffuse", ColorRGBA.White);
        ballGeom.setMaterial(ballMat);
        
        this.attachChild(ballGeom);
        createBasketball();
    }
    
    public void switchBall(BallChoice ballChoice){
        if (ballChoice != currentBallChoice){
            currentBallChoice = ballChoice;
            switch (ballChoice){
                case Bowlingball:
                    createBowlingball();
                    break;
                case Basketball:
                    createBasketball();
                    break;
                case Baseball:
                    createBaseBall();
                    break;
                case Tennisball:
                    createTennisball();
                    break;
                default:
                    break;
            }
            ballMat.setTexture("DiffuseMap", ballTexture);
        }
    }
    
    private void createBowlingball() {
        ballSphere.updateGeometry(50, 50, 0.3f);
        ballTexture = assetManager.loadTexture("Textures/bowlingball.png");
        main.updateRigidBall(0.3f, 5f, 0f);
    }
    private void createBasketball(){
        ballSphere.updateGeometry(50, 50, 0.3f);
        ballTexture = assetManager.loadTexture("Textures/BasketballColor.jpg");
        main.updateRigidBall(0.3f, 2f, 0.4f);
        
    }
    private void createBaseBall() {
        ballSphere.updateGeometry(50, 50, 0.1f);
        ballTexture = assetManager.loadTexture("Textures/SoftballColor.jpg");
        main.updateRigidBall(0.1f, 0.8f, 0.05f);
    }
    private void createTennisball() {
        ballSphere.updateGeometry(50, 50, 0.1f);
        ballTexture = assetManager.loadTexture("Textures/TennisBallColorMap.jpg");
        main.updateRigidBall(0.1f, 0.5f, 0.4f);
    }
}