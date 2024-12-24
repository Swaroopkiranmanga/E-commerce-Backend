package com.excelr.repository;

import com.excelr.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
}