package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;

public class Coin extends Node {
    private final AssetManager assetManager;
    
    public Coin(AssetManager assetManager){
        this.assetManager = assetManager;
        
        Cylinder coinShape = new Cylinder(10, 50, 0.1f, 0.02f, true);
        Geometry coinGeom = new Geometry("coinShape", coinShape);
        Material coinMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        coinMat.setColor("Diffuse", ColorRGBA.Yellow);
        coinMat.setBoolean("UseMaterialColors",true);
        coinGeom.setMaterial(coinMat);
        
        this.attachChild(coinGeom);
    }
}
