import java.lang.Exception

fun main() {
    val userTable = object : UsersTable {
        override fun insert() {}
        override fun delete() {}
    }
    transaction {
        with(userTable) {
            createUserTx()
        }
    }
}

fun transaction(function: Transaction.() -> Unit) {
    // Implementation of transaction logic by a library
}

fun Transaction.createUser(table: UsersTable) {
    try {
        table.insert()
        commit()
    } catch (e: Exception) {
        rollback()
        throw e
    }
}

fun UsersTable.createUser(tx: Transaction) {
    try {
        insert()
        tx.commit()
    } catch (e: Exception) {
        tx.rollback()
        throw e
    }
}

context(Transaction, UsersTable)
fun createUserTx() {
    try {
        insert()
        commit()
    } catch (e: Exception) {
        rollback()
        throw e
    }
}

interface Transaction {
    fun commit()

    fun rollback()
}

interface UsersTable {
    fun insert()
    fun delete()

    // More actions here
}