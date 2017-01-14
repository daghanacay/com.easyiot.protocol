# Configuration

{
     "service.factoryPid":"com.easyiot.auslora-websocket.device.service",
     "service.pid":<var> e.g. "local.auslora",
     "adminUrl":<var> e.g. "adm.auslora.com.au",
     "apiKey":<var> e.g. "AAAAEwz95eTEvbdkuxYEvxyLZBQQSTpeqwn3so8HT9os0XJDc"
}   

# REST Usage

- Get all devices for an application 

GET {host}/auslora/application/{ApplicationId}/devices

- Get single device for an application 

GET {host}/auslora/application/{ApplicationId}/device/{DeviceId}

- Delete device

DELETE {host}/auslora/application/{ApplicationId}/device/{DeviceId}

- Register an OTAA device

POST {host}/application/{ApplicationId}/device/otaa with body 
	{
		"deveui":"6717e4c27ed1db06",
		"appkey":"6717e4c27ed1db066717e4c27ed1db06"
	}
	
- Register an ABP device

POST {host}/application/{ApplicationId}/device/abp with body 
	{
		"deveui":"6717e4c27ed1db06",
		"devaddr":"6717e4c2",
		"seqno":"-1",
		"seqdn":"0",
		"appskey":"6717e4c27ed1db066717e4c27ed1db06",
		"nwkskey":"6717e4c27ed1db066717e4c27ed1db01"
	}