package de.redlion.civilwar.units;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.redlion.civilwar.Targeting;

public class EnemyAI extends DefaultAI {

	public EnemyAI(Soldier soldier) {
		super(soldier);
		retarget();
	}

	public void retarget() {
		target = Targeting.getNearestOfType(soldier, 0);
	}

	public void update() {
		if (target == null || !target.alive || MathUtils.random() < 0.005f) {
			retarget();
		}

		if (target != null) {			
			to_target.set(target.position.x - soldier.position.x, target.position.y - soldier.position.y);
			float dist_squared = to_target.dot(to_target);

			if (running) {
				// run away until you have full ammo and are far enough away
				boolean too_close = dist_squared < Math.pow(run_distance, 2);
				// if you're too close to the target then turn away
				if (too_close) {
					soldier.goAway(target.position, true);
				} else {
					soldier.thrust();
				}

				if (!soldier.isEmpty() && !too_close) {
					running = false;
				}
			} else {
				// go towards the target and attack!
				soldier.goTowards(target.position, true);

				// maybe shoot
				if (soldier.isReadyToShoot()) {
					if (dist_squared <= shot_range * shot_range && to_target.dot(soldier.facing) > 0 && Math.pow(to_target.dot(soldier.facing), 2) > 0.97 * dist_squared) {
						soldier.shoot();
					}
				}

				// if out of shots then run away
				if (soldier.isEmpty()) {
					running = true;
				}
			}
		}
	}
}
