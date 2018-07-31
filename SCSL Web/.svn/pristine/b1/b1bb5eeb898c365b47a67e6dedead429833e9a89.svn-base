/**
 * 
 */
package org.sdrc.scsl.repository.springdatajpa;

import java.util.List;

import org.sdrc.scsl.domain.TXNPDSA;
import org.sdrc.scsl.repository.TXNPDSARepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * @author Sarita Panigrahi (sarita@sdrc.co.in)
 */

@RepositoryDefinition(domainClass=TXNPDSA.class,idClass=Integer.class)
public interface SpringDataTXNPDSARepository extends TXNPDSARepository {

	
	@Override
	@Query("SELECT COUNT(txnpdsa.txnPDSAId),txnpdsa.pdsa.pdsaId FROM  TXNPDSA txnpdsa "
			+ " WHERE txnpdsa.pdsa.facility.areaId= :facilityId "
			+ " AND txnpdsa.percentage IS NOT NULL "
			+ " GROUP BY txnpdsa.pdsa.pdsaId ")
	List<Object[]> findCountByPercentageIsNotNullAndPdsaFacilityAreaIdGroupByPdsaPdsaId(
			@Param("facilityId")int facilityId);
	
//	@Override
//	@Query("SELECT CAST(txnpdsa.numeratorValue AS float) / CAST(txnpdsa.denominatorValue AS float), txnpdsa.txnPDSAId, txnpdsa.dueDate "
//			+ " FROM TXNPDSA txnpdsa "
//			+ " WHERE txnpdsa.pdsa.pdsaId= :pdsaId "
//			+ " AND txnpdsa.percentage IS NOT NULL")
//	List<Object[]> findFractionalIndexByFacilityAreaIdGroupByPdsaPdsaId(
//			@Param("pdsaId")Integer pdsaId, Pageable pageable);
	
//	@Override
//	@Query("SELECT CAST(SUM(pdsa.numeratorValue) AS float) / CAST(SUM(pdsa.denominatorValue) AS float) "
//			+ " FROM (SELECT txnpdsa.numeratorValue, txnpdsa.denominatorValue FROM TXNPDSA txnpdsa "
//			+ " WHERE txnpdsa.pdsa.pdsaId= :pdsaId AND txnpdsa.percentage IS NOT NULL ORDER BY DESC ) AS pdsa ")
//	List<Object[]> findAveragePByFacilityAreaIdGroupByPdsaPdsaId(
//			@Param("pdsaId")Integer pdsaId, Pageable pageable);
	
	@Override
	@Query(value="SELECT CAST(SUM(numerator_value) AS float) / CAST(SUM(denominator_value) AS float) "
			+ " FROM (SELECT numerator_value, denominator_value FROM txn_pdsa "
			+ " WHERE pdsa_id_fk = :pdsaId AND percentage IS NOT NULL ORDER BY id DESC limit 12) AS pdsa ", nativeQuery=true)
	Object findAveragePByFacilityAreaIdGroupByPdsaPdsaId(
			@Param("pdsaId")Integer pdsaId);
}
