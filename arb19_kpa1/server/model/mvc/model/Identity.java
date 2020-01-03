package arb19_kpa1.server.model.mvc.model;

import java.util.UUID;

import common.identity.IRoomIdentity;

/**
 * @author alexbluestein
 * Implementation of IRoomIdentity
 */
public class Identity implements IRoomIdentity {
	
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 2200086815588684560L;

	/**
	 * Name of room
	 */
	private String name;
	
	/**
	 * UUID of room
	 */
	private UUID uid;
	
	/**
	 * @param name Name of room
	 * @param uid UUID of room
	 */
	public Identity(String name, UUID uid) {
		this.name = name;
		this.uid = uid;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public UUID getID() {
		return uid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Identity other = (Identity) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

}
