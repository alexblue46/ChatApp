package common.virtualNetwork;

import java.io.Serializable;
import java.util.Collection;

/**
 * General interface for the nodes in a network graph
 * Really just a fancy named collection representing a virtual network
 * @param <TReceivers> Type of message receivers
 * @param <TIdentity> An IIdentity class
 */
public interface IVirtualNetwork<TReceivers, TIdentity> extends Serializable {
	
	/**
	 * Gets all members of the environment
	 * @return Collection of message receivers
	 */
	public Collection<TReceivers> getMembers();
	
    /**
     * @return The IIdentity class for the network
     */
    public TIdentity getInfo();

}
