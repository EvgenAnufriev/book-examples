/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.friend.impl


import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents
import sample.chirper.friend.api.FriendService

abstract class FriendModule (context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
  with CassandraPersistenceComponents
{
  override lazy val lagomServer: LagomServer = serverFor[FriendService](wire[FriendServiceImpl])
  persistentEntityRegistry.register(wire[FriendEntity])
  readSide.register(wire[FriendEventProcessor])

  override def jsonSerializerRegistry = ???
}

class FriendApplicationLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new FriendModule(context) with LagomDevModeComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new FriendModule(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[FriendService])
}

