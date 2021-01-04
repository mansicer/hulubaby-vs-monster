package Util;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import components.*;
import config.DawaConfig;
import config.WuwaConfig;
import org.junit.Test;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;
import util.ComponentUtils;
import util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class UtilTest {
    private Entity a;
    private Entity b;
    private Entity c;
    public UtilTest(){
        a = FXGL.entityBuilder()
                .at(300,300)
                .type(BasicEntityTypes.PLAYER)
                .with(new NetworkIDComponent("Dawa"))
                .with(new DetailedTypeComponent(DetailedEntityType.Dawa, CampType.HuluBabyCamp))
                .bbox(new HitBox(BoundingShape.box(60, 60)))
                .with(new ControllableComponent(250))
                .with(new MovableComponent(50, 50))
                .with(new MeleeAttackComponent(0.3, 0.4, 50, 100, 20))
                .collidable()
                .build();
        b =  FXGL.entityBuilder()
                .at(200,200)
                .type(BasicEntityTypes.PLAYER)
                .with(new NetworkIDComponent("WUWA"))
                .with(new DetailedTypeComponent(DetailedEntityType.Wuwa, CampType.HuluBabyCamp))
                .bbox(new HitBox(BoundingShape.box(60, 60)))
                .with(new ControllableComponent(1000))
                .collidable()
                .build();
        c = FXGL.entityBuilder()
                .at(200,200)
                .type(BasicEntityTypes.PLAYER)
                .with(new NetworkIDComponent("WUWA2"))
                .with(new DetailedTypeComponent(DetailedEntityType.Wuwa, CampType.MonsterCamp))
                .bbox(new HitBox(BoundingShape.box(60, 60)))
                .with(new ControllableComponent(1000))
                .collidable()
                .build();
    }

    @Test
    public void testIsEnemy(){
        assertTrue(!EntityUtils.isEnemy(a,b));
    }

    @Test
    public void testGetNetID(){
        assertSame(EntityUtils.getNetworkID(a),0);
        assertSame(EntityUtils.getNetworkID(b),1);
        assertSame(EntityUtils.getNetworkID(c),2);
    }

    @Test
    public void testGetMovableComponent(){
        assertNotEquals(ComponentUtils.getMovableComponent(b), Optional.empty());
        assertEquals(ComponentUtils.getMovableComponent(a), Optional.of(a.getComponent(MovableComponent.class)));
    }

}
