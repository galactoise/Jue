Juex
===

Juex, or "Java Hue Extended", is a fork of Q42's super solid Java client for Philips Hue bridge interaction.  The extension is basically two-fold - mavenizing it for ease of use in other projects, and adding convenience methods to retrieve individual objects by id rather.  The original project assumed that a caller would have knowledge of full state at the time of making a call to retrieve an object, and can therefore pass an object representing a superset of information about what is being retrieved.  However, Juex is being used as the backing for a REST API, which is innately stateless, and so lookup by object id is necessary.

The original project can be found here: (https://github.com/Q42/Jue)
