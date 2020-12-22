package components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.control.ProgressBar;

public class HealthComponent extends Component {
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void increaseHealth(int inc) {
        this.health += inc;
        this.health = Math.max(this.health, maxHealth);
    }

    public void decreaseHealth(int dec) {
        this.health -= dec;
        if (this.health <= 0) {
            entity.removeFromWorld();

        }
    }

    @Override
    public void onUpdate(double tpf) {
        healthUI.setProgress((double) health / maxHealth);
    }
}
