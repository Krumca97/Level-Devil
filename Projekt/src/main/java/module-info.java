module projekt {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires static lombok;
    requires jakarta.persistence;
    requires spring.data.jpa;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires org.slf4j;
    requires spring.web;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires org.jspecify;
    opens projekt.frontEnd to javafx.fxml;
    exports projekt.frontEnd;
    exports projekt.frontEnd.history;
    opens projekt.frontEnd.history to javafx.fxml;
    opens projekt.backEnd;
    opens projekt.backEnd.controllers;
    opens projekt.backEnd.entities;
    opens projekt.backEnd.repositories;
}