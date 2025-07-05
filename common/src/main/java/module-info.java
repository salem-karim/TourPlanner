module at.technikum.common {
  exports at.technikum.common.DAL.models;
//  exports at.technikum.common.custom;
  requires static lombok;
    requires jakarta.persistence;
  requires org.hibernate.orm.core;
  requires com.fasterxml.jackson.annotation;
}