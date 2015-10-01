package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class Parcour extends Node{
    private final AssetManager assetManager;
    
    private final Material wood;
    
    public Parcour(AssetManager assetManager){
        this.assetManager = assetManager;
        
        wood = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        Texture woodTexture = assetManager.loadTexture("Textures/bamboo-flooring-texture-seamless-fa2c6r2ql.jpg");
        woodTexture.setWrap(Texture.WrapAxis.S, Texture.WrapMode.Repeat);
        wood.setTexture("DiffuseMap", woodTexture);
        wood.setColor("Diffuse", ColorRGBA.White);
        
        createStartPlatform(0, 0, -4);
    }
    private void createStartPlatform(float x, float y, float z){
        Box b = new Box(1, 0.1f, 5);
        b.scaleTextureCoordinates(new Vector2f(5, 1));
        Geometry geom = new Geometry("Box", b);
        geom.setLocalTranslation(x, y, z);
        geom.setMaterial(wood);
        this.attachChild(geom);
    }
}
