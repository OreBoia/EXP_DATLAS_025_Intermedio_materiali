package com.shopflow.ordini.interno;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

// Interfaccia Spring Data: la conosce solo l'adattatore
interface OrdineJpaRepository extends JpaRepository<OrdineEntity, UUID> {}
