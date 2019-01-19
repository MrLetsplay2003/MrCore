class Webinterface {
	
	static callback(target, id, data = {}) {
		$.ajax({
			url: "/internal/callback",
			data: {target: target, id: id, data: Webinterface.serialize(data)},
			success: (response) => {
				console.log("Got callback response", response);
			},
			error: () => {
				console.log("Callback failed");
			}
		});
	}
	
	static serialize(object) {
		return JSON.stringify(object);
	}
	
}

setInterval(() => {
	console.log("Update!");
}, 1000);