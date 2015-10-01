package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

public class Ball extends Node {
    private final AssetManager assetManager;
    
    private Sphere ballSphere;
    private Geometry ballGeom;
    private Texture ballTexture;
    private Material ballMat;
    
    public Ball(AssetManager assetManager, BallChoice ballChoice){
        this.assetManager = assetManager;
        
        switch (ballChoice){
            case Bowlingball:
                break;
            case Basketball:
                createBasketball();
                break;
            case Baseball:
                break;
            case Tennisball:
                break;
            default:
                break;
        }
    }
    
    private void createBasketball(){
        ballSphere = new Sphere(20, 20, 0.3f);
        ballGeom = new Geometry("ballSphere", ballSphere);
        ballTexture = assetManager.loadTexture("Textures/BasketballColor.jpg");
        ballMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        ballMat.setTexture("DiffuseMap", ballTexture);
        ballMat.setColor("Diffuse", ColorRGBA.White);
        ballGeom.setMaterial(ballMat);
        this.attachChild(ballGeom);
    }
}