package com.excelr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excelr.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}