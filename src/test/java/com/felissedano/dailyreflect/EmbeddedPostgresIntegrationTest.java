//package com.felissedano.dailyreflect;
//
//import com.felissedano.dailyreflect.configurations.embeddedpg.EmbeddedPostgresConfiguration;
//import com.felissedano.dailyreflect.models.Journal;
//import com.felissedano.dailyreflect.repositories.JournalRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@DataJpaTest
//@ExtendWith(EmbeddedPostgresConfiguration.EmbeddedPostgresExtension.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ContextConfiguration(classes = {EmbeddedPostgresConfiguration.class})
//public class EmbeddedPostgresIntegrationTest {
//    @Autowired
//    private JournalRepository repository;
//
//    @Test
//    void givenEmbeddedPostgres_whenSavePerson_thenSavedEntityShouldBeReturnedWithExpectedFields(){
//        Date date = new GregorianCalendar(2010, Calendar.FEBRUARY, 10).getTime();
//        Journal journal = new Journal(date, "Hello", new ArrayList<>());
//
//        Journal savedJournal = repository.save(journal);
//        assertEquals(journal.getContent(), savedJournal.getContent());
//    }
//}