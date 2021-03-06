package com.cibernet.splatcraft.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class SplatCraftParticleSpawner
{
    private static Minecraft mc = Minecraft.getMinecraft();

    public static Particle spawnInkParticle(double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color)
    {
        return spawnInkParticle(xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, color, 1);
    }

    public static Particle spawnInkParticle(double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color, float size) {
        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null) {
            int partSetting = mc.gameSettings.particleSetting;

            if (partSetting == 1 && mc.world.rand.nextInt(3) == 0) {
                partSetting = 2;
            }

            double xCheck = mc.getRenderViewEntity().posX - xCoordIn;
            double yCheck = mc.getRenderViewEntity().posY - yCoordIn;
            double zCheck = mc.getRenderViewEntity().posZ - zCoordIn;
            Particle particle = null;
            double max = 16.0D;

            if (xCheck * xCheck + yCheck * yCheck + zCheck * zCheck > max * max) {
                return null;
            } else if (partSetting > 1) {
                return null;
            } else {

                particle = new ParticleInk(mc.world, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, color, size);
                mc.effectRenderer.addEffect(particle);
                return particle;
            }
        }
        return null;
    }
    public static Particle spawnSquidSwimParticle(double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color, float size) {
        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null) {
            int partSetting = mc.gameSettings.particleSetting;
            
            if(partSetting == 2 && mc.world.rand.nextInt(2) == 0)
                partSetting = 0;
            else if (partSetting == 1 && mc.world.rand.nextInt(3) == 0) {
                partSetting = 2;
            }
            
            double xCheck = mc.getRenderViewEntity().posX - xCoordIn;
            double yCheck = mc.getRenderViewEntity().posY - yCoordIn;
            double zCheck = mc.getRenderViewEntity().posZ - zCoordIn;
            Particle particle = null;
            double max = 16.0D;
            
            if (xCheck * xCheck + yCheck * yCheck + zCheck * zCheck > max * max) {
                return null;
            } else if (partSetting > 1) {
                return null;
            } else {
                
                particle = new ParticleInk(mc.world, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, color, size);
                mc.effectRenderer.addEffect(particle);
                return particle;
            }
        }
        return null;
    }
    
    public static Particle spawnInksplosionParticle(double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color, float size) {
        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null) {
            int partSetting = mc.gameSettings.particleSetting;
            
            if (partSetting == 1 && mc.world.rand.nextInt(3) == 0)
                partSetting = 2;
            
            
            double xCheck = mc.getRenderViewEntity().posX - xCoordIn;
            double yCheck = mc.getRenderViewEntity().posY - yCoordIn;
            double zCheck = mc.getRenderViewEntity().posZ - zCoordIn;
            Particle particle = null;
            double max = 16.0D;
            
            if (xCheck * xCheck + yCheck * yCheck + zCheck * zCheck > max * max) {
                return null;
            } else {
                
                particle = new ParticleInksplosion(mc.world, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, color, size);
                mc.effectRenderer.addEffect(particle);
                return particle;
            }
        }
        return null;
    }
    
    public static Particle spawnSquidSoulParticle(double xCoordIn, double yCoordIn, double zCoordIn, int color) {
        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null) {
            int partSetting = mc.gameSettings.particleSetting;
            
            if (partSetting == 1 && mc.world.rand.nextInt(3) == 0)
                partSetting = 2;
            
            
            double xCheck = mc.getRenderViewEntity().posX - xCoordIn;
            double yCheck = mc.getRenderViewEntity().posY - yCoordIn;
            double zCheck = mc.getRenderViewEntity().posZ - zCoordIn;
            Particle particle = null;
            double max = 16.0D;
            
            if (xCheck * xCheck + yCheck * yCheck + zCheck * zCheck > max * max) {
                return null;
            } else {
                
                particle = new ParticleSquidSoul(mc.world, xCoordIn, yCoordIn, zCoordIn, color);
                mc.effectRenderer.addEffect(particle);
                return particle;
            }
        }
        return null;
    }
}
