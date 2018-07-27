package org.sdrc.scsl.repository.springdatajpa;

import java.util.Date;
import java.util.List;

import org.sdrc.scsl.domain.Type;
import org.sdrc.scsl.repository.TypeRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:18:25 am
 */
@RepositoryDefinition(domainClass=Type.class,idClass=Integer.class)
public interface SpringDataTypeRepository extends TypeRepository {

	@Override
	@Query("SELECT t FROM Type t WHERE t.createdDate > :date or t.updatedDate > :date")
	public List<Type> findByLastSyncDate(@Param("date") Date lastSyncDate);
}
