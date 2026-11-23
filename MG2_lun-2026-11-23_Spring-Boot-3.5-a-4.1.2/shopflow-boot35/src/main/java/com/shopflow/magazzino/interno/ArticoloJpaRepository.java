package com.shopflow.magazzino.interno;

import org.springframework.data.jpa.repository.JpaRepository;

interface ArticoloJpaRepository extends JpaRepository<ArticoloEntity, String> {}
