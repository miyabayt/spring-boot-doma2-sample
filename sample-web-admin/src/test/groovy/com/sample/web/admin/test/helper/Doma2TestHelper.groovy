package com.sample.web.admin.test.helper

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.builder.DeleteBuilder
import org.seasar.doma.jdbc.builder.InsertBuilder
import org.seasar.doma.jdbc.builder.UpdateBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Doma2TestHelper {
    @Autowired
    Config config

    def delete(String table){
        DeleteBuilder.newInstance(config).sql('delete from ' + toSnakeCase(table)).execute()
    }

    def insert(String table, Map<String,String> columns){
        if(columns == null || columns.isEmpty()){
            return
        }
        def c = columns.collect { toSnakeCase(it.key) }
                .inject {s,k -> s + ',' + k }
        def v = columns.collect { it.value }
                .collect {v -> v == null ? 'NULL' : '\'' + v + '\''}
                .inject {s, v -> s + ',' + v }
        InsertBuilder.newInstance(config).sql('insert into ' + toSnakeCase(table) + '(' + c + ') VALUES (' + v + ')').execute()
    }

    def update(String table, Map<String,String> columns, Map<String,String> conditions){
        if(columns == null || columns.isEmpty()){
            return
        }
        def updated = columns.collect { toSnakeCase(it.key) + '=\'' + it.value + '\''}
                .inject {s,v -> s + ',' + v }
        def where = conditions.collect { toSnakeCase(it.key) + '=\'' + it.value + '\''}
                .inject {s, v -> s + ',' + v }
        UpdateBuilder.newInstance(config).sql('update ' + toSnakeCase(table) + ' set ' + updated + ' where ' + where).execute()
    }

    static String toSnakeCase( String text ) {
        text.replaceAll( /([A-Z])/, /_$1/ ).toLowerCase().replaceAll( /^_/, '' )
    }
}
