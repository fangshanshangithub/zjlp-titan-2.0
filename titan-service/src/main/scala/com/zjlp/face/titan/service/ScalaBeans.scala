package com.zjlp.face.titan.service


case class UserVertexId(val userId: String, val vertexId: String)

case class FriendShip(val userId: String, val friendUserId: String)

case class IfCache(val userId: String, val isCached: Boolean)