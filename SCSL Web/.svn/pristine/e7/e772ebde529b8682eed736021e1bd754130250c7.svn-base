package org.sdrc.scsl.repository.springdatajpa;

import java.util.Date;
import java.util.List;

import org.sdrc.scsl.domain.TypeDetail;
import org.sdrc.scsl.repository.TypeDetailRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:21:54 am
 */
@RepositoryDefinition(domainClass=TypeDetail.class,idClass=Integer.class)
public interface SpringDataTypeDetailRepository extends TypeDetailRepository {

	@Override
	@Query("SELECT t FROM TypeDetail t WHERE t.createdDate > :date or t.updatedDate > :date")
	public List<TypeDetail> findByLastSyncDate(@Param("date") Date lastSyncDate);
}
