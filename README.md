RSC SSH Tunneler
================

## Abstract

We wrote this plugin as a workaround for a issue related to debugging PHP code over a VPN.  Due to the nature of how XDebug works,
debugging is not possible when working over a NATed VPN, such as an IpSec VPN.  This plugin will allow the user to establish
an SSH tunnel to the server to connect to an instance of DBGp, which will proxy debug requests to the XDebug service.  The
end result is that debuggin will then work over the VPN.

## Disclaimer

*I am not a Java developer.*

Let me say that again.

*I am not a Java developer.*

This is my first Java project.  As such, it may be buggy.  It may have issues.  It may have oversights.  It may not
stick to any form of best practices.  It may not work at all.

You assume all risk from using this plugin.  I make no warranties or guarantees regarding its quality or fitness for use.
RSC is not liable for ANY issues resulting from the use of this plugin.  You're completely on your own if something blows up.

## Requirements

This requires that a DBGp proxy be installed and configured somewhere on the local network.  For this documentation, we will
assume that DBGp is installed on the same server as Apache.  If this is not the case in your environment, adjust the settings
as needed to facilitate the connection.  This documentation will also assume that you have an existing VPN setup, and that
you are connected to the VPN during all parts of the configurion, except where otherwise noted.

Obviously, you will also need a copy of an IntelliJ-based IDE to use this plugin.  See www.jetbrains.com for an IDE that will
This plugin was designed with PHPStorm in mind, but will work with and IntelliJ family IDE.

Additionally, this plugin establishes TWO forms of tunnel -- A local tunel and remote tunnel.  The remote tunnel binds to a
specific loopback adapter address.  To allow this, you will need to edit your SSH configuration (located by default at /etc/ssh/sshd_config) to set `GatewayPorts`
to `clientspecified`.  Note that if you have any other services that use SSH tunneling, this will change the syntax of
ssh connection strings when creating a remote tunnel.  Simply add a leading colon to the any remote tunnel creation requsts (like `ssh -R :9000:localhost:9000`)

Build depends on an IntelliJ SDK and the JSCh library, available at http://www.jcraft.com/jsch/.

## Configuration

Once requirements have been met, Install the plugin via the IDE's settings pane.  Use the 'Install from disk' button in the
plugins section.  Once the plugin is installed, restart your IDE.

Next, open the IDE's settings pane again, and locate the new section titled 'RSC SSH Tunnel Manager'.  It may be located
under 'Other Settings.'  Configure the settings for your network as follows:

| Field Name              | What it does                                                                                                                       |
|-------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| **Remote Host:**        | This is the IP or DNS address of the server hosting your XDebug instance.                                                          | 
| **IDE Port:**           | This is the port that XDebug expects your IDE to be listening for debug session on.  Default is 9001.                              |
| **XDebug Port:**        | This is the port that XDebug communicates with Apache on.  See your Apache and XDebug configuration.  Default is 9000.             |
| **Username:**           | Username of the SSH user to use.                                                                                                   |
| **Password:**           | Password for the SSH user.                                                                                                         |
| **Loopback Interface:** | See the 'Loopback Interface' section below.  The plugin will choose a random interface at every connection if you leave this blank.|

Click Ok to save and exit.

#### The Loopback Interface

This plugin works by binding both a client-side and server-side SSH tunnel to a loopback adapter interface.  By default on
linux servers, this can be any IP address in the 127.0.0.0/8 subnet.  However, your server configuration may vary.

You may specify a loopback IP to use.  If you do so, the same address will be used for all tunnels.  This may save you from
having to register and unregister your IDE with DBGp every time you connect.

If you do not specify a loopback IP, the plugin will choose one automatically at random.  Note that this may result in invalid IPs
being chosen if your server does not allow any IP in 127.0.0.0/8 to loopback.  Also please note that you will need to register
and unregister your IDE with every tunnel connection, as DBGp will associate your IDE key with your loopback address.

## Usage

Usage is very simple.  Once configured, simply go to Tools->Manage RSC SSH Tunnel->Open SSH Tunnel to Server to open your SSH tunnel.
If all goes well, you will be notified that you have been connected.  You may then XDebug as normal.

Once you are finished, go to Tools->Manage RSC SSH Tunnel->Close SSH Tunnel to disconnect.

## DBGp Help

Installing DBGp is beyond the scope of this document.  [Here](http://matthardy.net/blog/configuring-phpstorm-xdebug-dbgp-proxy-settings-remote-debugging-multiple-users/) is a
fairly decent howto on the subject.

## Contributions

Contributions are gladly accepted. Please submit a pull request on the development branch.

## License

This module is licensed under the Apache 2.0 license, a copy is included in the repository.
