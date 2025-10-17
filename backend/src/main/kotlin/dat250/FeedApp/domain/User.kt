package dat250.FeedApp.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity @Table(name = "USERS") data class User(@Id val username: String)
