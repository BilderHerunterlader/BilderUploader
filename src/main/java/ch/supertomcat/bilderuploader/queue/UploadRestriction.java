package ch.supertomcat.bilderuploader.queue;

import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.supertomcatutils.queue.RestrictionBase;

/**
 * Upload Restriction
 */
public class UploadRestriction extends RestrictionBase {
	/**
	 * Constructor
	 * 
	 * @param hoster Hoster
	 */
	public UploadRestriction(Hoster hoster) {
		super(hoster.getName(), hoster.getMaxConnections() != null ? hoster.getMaxConnections() : 0);
	}
}
