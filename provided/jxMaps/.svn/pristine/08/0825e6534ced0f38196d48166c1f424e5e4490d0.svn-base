package provided.jxMaps.utils.impl;

import provided.jxMaps.utils.IAction;
import provided.util.logic.ILogicalHost;
import provided.util.logic.ILogicalVisitor;

/**
 * An IAction that delegates to an ILogicalHost and an ILogicalVisitor.
 * The parameter this action is called with is passed along as the visitor's input parameter.
 * @author swong
 *
 * @param <T>  The type of the input parameter for the accept method of the IAction.accept() method.
 */
public class LogicalAction<T> implements IAction<T> {
	
	/**
	 * The logical host object whose state will determine what case of the visitor is called when the action's accept method is called.
	 */
	private ILogicalHost logicHost;
	/**
	 * The logical visitor whose cases will provide the true and false case processing.
	 */
	private ILogicalVisitor<Void, T> logicVisitor;
	
	/**
	 * Construct an IAction handler using the given logical host and visitor.
	 * @param logicHost The logical host to use
	 * @param logicVisitor The logical visitor to use
	 */
	public LogicalAction(ILogicalHost logicHost, ILogicalVisitor<Void, T> logicVisitor) {
		this.logicHost = logicHost;
		this.logicVisitor = logicVisitor;
	}

	@Override
	public void accept(T param) {
		logicHost.execute(logicVisitor, param);		
	}

}
