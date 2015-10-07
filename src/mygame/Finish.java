package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

public class Finish extends Node {
    private Sphere finishSphere;
    private Geometry finishGeom;
    private Material finishMat;
    
    private float radius = 0.5f;
    
    public Finish(AssetManager assetManager){
        finishSphere = new Sphere(20, 20, radius);
        finishGeom = new Geometry("finishSphere", finishSphere);
        finishMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        finishMat.setColor("Color", new ColorRGBA(0.3f, 0.3f, 1, 0.5f));
        finishMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        finishGeom.setMaterial(finishMat);
        finishGeom.setQueueBucket(RenderQueue.Bucket.Translucent);
        this.attachChild(finishGeom);
    }
    public float getRadius(){
        return radius;
    }
}
