package org.sdrc.scsl.repository.springdatajpa;

import java.util.List;

import org.sdrc.scsl.domain.PDSA;
import org.sdrc.scsl.repository.PDSARepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
/**
 * 
 * @author Mandakini Biswal
 * @author Sarita Panigrahi (sarita@sdrc.co.in)
 *
 */
@RepositoryDefinition(domainClass=PDSA.class,idClass=Integer.class)
public interface SpringDataPDSARepository extends PDSARepository {
	
	@Override
	@Query("SELECT DISTINCT pds.facility.areaId FROM PDSA pds")
	List<Integer> findDistinctFacilityId();
	
}
