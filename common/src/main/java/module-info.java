module at.technikum.common {
  exports at.technikum.common.models;
//  exports at.technikum.common.utils;
  requires static lombok;
    requires jakarta.persistence;
  requires org.hibernate.orm.core;
}