package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.SerializableComponent;
import javafx.scene.control.ProgressBar;
import org.jetbrains.annotations.NotNull;

public class HealthComponent extends Component implements SerializableComponent {
    protected int health = 100;
    protected int maxHealth = 100;
    protected boolean isVisible = true;
    protected ProgressBar healthUI;

    public HealthComponent(int maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public HealthComponent(int maxHealth, boolean isVisible) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.isVisible = isVisible;
    }

    @Override
    public void onAdded() {
        if (isVisible) {
            healthUI = new ProgressBar((double) health / maxHealth);
            healthUI.setLayoutX(0);
            healthUI.setLayoutY(-10);
            healthUI.setPrefWidth(entity.getWidth());
            healthUI.setPrefHeight(10);
            entity.getViewComponent().addChild(healthUI);
        }
    }

    public void checkHealth() {
        this.health = Math.min(this.health, maxHealth);
        if (this.health <= 0) {
            entity.removeFromWorld();
        }
    }

    public void increaseHealth(int inc) {
        this.health += inc;
        checkHealth();
    }

    public void decreaseHealth(int dec) {
        this.health -= dec;
        checkHealth();
    }

    @Override
    public void onUpdate(double tpf) {
        healthUI.setProgress((double) health / maxHealth);
        if (entity.getScaleX() < 0) {
            healthUI.setScaleX(-1);
        } else if (entity.getScaleX() > 0) {
            healthUI.setScaleX(1);
        }
    }

    @Override
    public void read(@NotNull Bundle bundle) {
        health = bundle.get("health");
    }

    @Override
    public void write(@NotNull Bundle bundle) {
        bundle.put("health", health);
    }
}
