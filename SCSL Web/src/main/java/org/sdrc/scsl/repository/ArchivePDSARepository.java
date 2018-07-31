/**
 * 
 */
package org.sdrc.scsl.repository;

import org.sdrc.scsl.domain.ArchivePDSA;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public interface ArchivePDSARepository {

	/**
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 * @param archivePdsa
	 * @return
	 */
	@Transactional
	ArchivePDSA save(ArchivePDSA archivePdsa);

}
