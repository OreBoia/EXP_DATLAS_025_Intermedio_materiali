package com.shopflow;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

// MG3: i confini si controllano nella build, non in revisione
class ArchitetturaTest {

    // Analizza il codice e ricava i moduli dai sotto-package della classe principale
    ApplicationModules moduli = ApplicationModules.of(ShopFlowApplication.class);

    private final JavaClasses classi = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())    // solo codice di produzione
            .importPackages("com.shopflow");

    @Test
    void confiniRispettati() {
        moduli.verify();    // fallisce se: cicli tra moduli, accesso a "interno", dipendenze non consentite
    }

    @Test
    void moduliAttesi() {
        moduli.forEach(m -> System.out.println(m.getIdentifier() + " -> " + m.getBasePackage().getName()));
        org.assertj.core.api.Assertions.assertThat(moduli.stream().map(m -> m.getIdentifier().toString()))
                .containsExactlyInAnyOrder("comune", "ordini", "magazzino", "pagamenti");
    }

    @Test
    void dominioSenzaFramework() {
        noClasses().that().resideInAPackage("..ordini.dominio..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("org.springframework..", "jakarta.persistence..")
            .because("il dominio deve restare testabile senza framework")
            .check(classi);                                            // lancia AssertionError se violata
    }
}
