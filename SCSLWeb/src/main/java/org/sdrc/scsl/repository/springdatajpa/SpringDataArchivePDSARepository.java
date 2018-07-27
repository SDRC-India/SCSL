/**
 * 
 */
package org.sdrc.scsl.repository.springdatajpa;

import org.sdrc.scsl.domain.ArchivePDSA;
import org.sdrc.scsl.repository.ArchivePDSARepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
@RepositoryDefinition(domainClass=ArchivePDSA.class,idClass=Integer.class)
public interface SpringDataArchivePDSARepository extends ArchivePDSARepository {

}
