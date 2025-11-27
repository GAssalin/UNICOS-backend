package br.com.unicos.ms_auth.repository;

import br.com.unicos.ms_auth.model.RoleHierarchyRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleHierarchyRelationRepository extends JpaRepository<RoleHierarchyRelation, Long> {
}
