package com.badlogic.gdx.physics.bullet.dynamics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btVector3;

/** @author xpenatan */
public class btDynamicsWorld extends btCollisionWorld {

	/*JNI
		#include <src/bullet/BulletDynamics/Dynamics/btDynamicsWorld.h>
		#include <src/custom/gdx/common/envHelper.h>
	*/
	
	btConstraintSolver constraintSolver;
	
	public int stepSimulation(float timeStep, int maxSubSteps, float fixedTimeStep) {
		return stepSimulation(cPointer, timeStep, maxSubSteps, fixedTimeStep);
	}
	
	public int stepSimulation(float timeStep, int maxSubSteps) {
		return stepSimulation(cPointer, timeStep, maxSubSteps, 1.0f/60.0f);
	}
	
	public int stepSimulation(float timeStep) {
		return stepSimulation(cPointer, timeStep, 1, 1.0f/60.0f);
	}
	
	private native int stepSimulation(long addr, float timeStep, int maxSubSteps, float fixedTimeStep); /*
		EnvHelper::env = env;
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		jint ret = world->stepSimulation(timeStep, maxSubSteps, fixedTimeStep);
		EnvHelper::env = 0;
		return ret;
	*/
	/*[0;X;L]
		jsObj, this.jsObj #P
		return jsObj.stepSimulation(timeStep,maxSubSteps,fixedTimeStep);
	*/
	
	public void addConstraint(btTypedConstraint constraint, boolean disableCollisionsBetweenLinkedBodies) {
		addConstraint(cPointer, constraint.cPointer, disableCollisionsBetweenLinkedBodies);
	}
	
	public void addConstraint(btTypedConstraint constraint) {
		addConstraint(cPointer, constraint.cPointer, false);
	}

	private native void addConstraint(long addr, long typedConstraintAddr, boolean disableCollisionsBetweenLinkedBodies); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		btTypedConstraint * typedConstraint = (btTypedConstraint *)typedConstraintAddr;
		world->addConstraint(typedConstraint, disableCollisionsBetweenLinkedBodies);
	*/
	/*[0;X;L]
		jsObj, this.jsObj #P
		var typedCon = Bullet.wrapPointer(typedConstraintAddr, Bullet.btTypedConstraint);
		jsObj.addConstraint(typedCon, disableCollisionsBetweenLinkedBodies);
	*/
	
	public void removeConstraint(btTypedConstraint constraint) {
		removeConstraint(cPointer, constraint.cPointer);
	}
	
	private native void removeConstraint(long addr, long typedConstraintAddr); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		btTypedConstraint * typedConstraint = (btTypedConstraint *)typedConstraintAddr;
		world->removeConstraint(typedConstraint);
	*/
	/*[0;X;L]
		jsObj, this.jsObj #P
		var typedCon = Bullet.wrapPointer(typedConstraintAddr, Bullet.btTypedConstraint);
		jsObj.removeConstraint(typedCon);
	*/
	
	public void addAction(btActionInterface action) {
		addAction(cPointer, action.cPointer);
	}
	/*[0;X;L]
		jsObj, this.jsObj #P
		jsAct, action.jsObj #P
		jsObj.addAction(jsAct);
	*/
	
	private static native void addAction(long addr, long actionAddr); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		btActionInterface * action = (btActionInterface *)actionAddr;
		world->addAction(action);
	*/
	/*[0;X;D]*/

	public void removeAction(btActionInterface action) {
		removeAction(cPointer, action.cPointer);
	}
	/*[0;X;L]
		jsObj, this.jsObj #P
		jsAct, action.jsObj #P
		jsObj.removeAction(jsAct);
	 */
	
	private static native void removeAction(long addr, long actionAddr); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		btActionInterface * action = (btActionInterface *)actionAddr;
		world->removeAction(action);
	*/
	/*[0;X;D]*/

	public void setGravity(Vector3 gravity) {
		checkPointer();
		setGravity(cPointer, gravity.x, gravity.y, gravity.z);
	}
	/*[0;X;L]
 		checkPointer(); #J
 		jsObj, this.jsObj #P
 		x, gravity.x #P
 		y, gravity.y #P 
 		z, gravity.z #P
		jsObj.setGravity(Bullet.MyTemp.prototype.btVec3_1(x,y,z));
	*/
	
	public void setGravity(float x, float y, float z) {
		checkPointer();
		setGravity(cPointer, x, y, z);
	}
	/*[0;X;L]
 		checkPointer(); #J
 		jsObj, this.jsObj #P
		jsObj.setGravity(Bullet.MyTemp.prototype.btVec3_1(x,y,z));
	*/
	
	private static native void setGravity(long addr, float x, float y, float z); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		btVector3 gravity;
		gravity.setValue(x,y,z);
		world->setGravity(gravity);
	*/
	/*[0;X;D]*/

	public void getGravity(Vector3 out) {
		checkPointer();
		getGravity(cPointer, btVector3.localArr_1);
		out.set(btVector3.localArr_1);
	}
	/*[0;X;L]
 		checkPointer(); #J
 		jsObj, this.jsObj #P
		var gravity = jsObj.getGravity();
		out.x = gravity.x(); #EVALFLOAT
		out.y = gravity.y(); #EVALFLOAT
		out.z = gravity.z(); #EVALFLOAT
	*/
	
	private static native void getGravity(long addr, float [] value); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		btVector3 vec = world->getGravity();
		value[0] = vec.getX();
		value[1] = vec.getY();
		value[2] = vec.getZ();
	*/
	/*[0;X;D]*/
	
	public void addRigidBody(btRigidBody body) {
		checkPointer();
		addBody(body);
		addRigidBody(cPointer, body.cPointer);
	}
	/*[0;X;L]
	 	checkPointer(); #J
 		addBody(body); #J
 		jsObj, this.jsObj #P
 		jsBod, body.jsObj #P
		jsObj.addRigidBody(jsBod);
	*/
	
	private static native void addRigidBody(long addr, long bodyAddr); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		btRigidBody * body = (btRigidBody *)bodyAddr;
		world->addRigidBody(body);
	*/
	/*[0;X;D]*/
	
	public void addRigidBody(btRigidBody body, short group, short mask) {
		checkPointer();
		addBody(body);
		addRigidBody(cPointer, body.cPointer, group, mask);
	}
	/*[0;X;L]
 		checkPointer(); #J
 		addBody(body); #J
 		jsObj, this.jsObj #P
 		jsBod, body.jsObj #P
		jsObj.addRigidBody(jsBod,group,mask);
	*/

	private static native void addRigidBody(long addr, long bodyAddr, short group, short mask); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		btRigidBody * body = (btRigidBody *)bodyAddr;
		world->addRigidBody(body, group, mask);
	*/
	/*[0;X;D]*/
	
	public void removeRigidBody(btRigidBody body) {
		checkPointer();
		removeBody(body);
		removeRigidBody(cPointer, body.cPointer);
	}
	/*[0;X;L]
 		checkPointer(); #J
 		removeBody(body); #J
 		jsObj, this.jsObj #P
 		jsBod, body.jsObj #P
		jsObj.removeRigidBody(jsBod);
	*/

	private static native void removeRigidBody(long addr, long bodyAddr); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		btRigidBody * body = (btRigidBody *)bodyAddr;
		world->removeRigidBody(body);
	*/
	/*[0;X;D]*/
	
	public void setConstraintSolver(btConstraintSolver solver) {
		checkPointer();
		this.constraintSolver = solver;
		setConstraintSolver(cPointer, solver.cPointer);
	}
	/*[0;X;L]
 		checkPointer(); #J
 		this.constraintSolver = solver; #J
 		jsObj, this.jsObj #P
 		jsSol, solver.jsObj #P
		jsObj.setConstraintSolver(jsSol);
	*/

	private static native void setConstraintSolver(long addr, long solverAddr); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		btConstraintSolver * solver = (btConstraintSolver *)solverAddr;
		world->setConstraintSolver(solver);
	*/
	/*[0;X;D]*/
	
	public btConstraintSolver getConstraintSolver() {
		return constraintSolver;
	}
	
	public int getNumConstraints() {
		checkPointer();
		return getNumConstraints(cPointer);
	}
	/*[0;X;L]
 		checkPointer(); #J
 		jsObj, this.jsObj #P
		return jsObj.getNumConstraints();
	*/

	private static native int getNumConstraints(long addr); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		return world->getNumConstraints();
	*/
	/*[0;X;D]*/
	
	public void clearForces() {
		checkPointer();
		clearForces(cPointer);
	}
	/*[0;X;L]
 		checkPointer(); #J
 		jsObj, this.jsObj #P
		jsObj.clearForces();
	*/
	
	private static native void clearForces(long addr); /*
		btDynamicsWorld * world = (btDynamicsWorld *)addr;
		world->clearForces();
	*/
	/*[0;X;D]*/
}
