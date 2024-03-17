package com.example.cats

import com.example.Cat
import com.example.CatsTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

interface CatsService {
    fun findAll(): List<Cat>
    fun find(id: Int): Cat?
    fun create(name: String, age: Int): EntityID<Int>

    fun delete(id: Int): Int

    fun update(id: Int, name: String, age: Int): Int
}

class CatsServiceImpl : CatsService {
    override fun findAll(): List<Cat> {
        return transaction {
            CatsTable.selectAll().map { row ->
                Cat(
                    row[CatsTable.id].value,
                    row[CatsTable.name],
                    row[CatsTable.age]
                )
            }
        }
    }

    override fun find(id: Int): Cat? {
        return transaction {
            val row = CatsTable.select {
                CatsTable.id eq id
            }.firstOrNull()

            if (row != null) {
                Cat(
                    row[CatsTable.id].value,
                    row[CatsTable.name],
                    row[CatsTable.age]
                )
            } else {
                null
            }
        }
    }

    override fun create(name: String, age: Int): EntityID<Int> {
        return transaction {
            CatsTable.insertAndGetId { cat ->
                cat[CatsTable.name] = name
                cat[CatsTable.age] = age
            }
        }
    }

    override fun delete(id: Int): Int {
        return transaction {
            CatsTable.deleteWhere { CatsTable.id eq id }
        }
    }

    override fun update(id: Int, name: String, age: Int): Int = transaction {
        CatsTable.update({ CatsTable.id eq id }) { cat ->
            cat[CatsTable.name] = name
            cat[CatsTable.age] = age
        }
    }
}

