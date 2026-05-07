document.addEventListener('DOMContentLoaded', function() {
    loadFriendList();
});

function loadFriendList() {
fetch(`${CTX}/api/friends`)
    	.then(res => res.json())
    	.then(renderFriends);
}

function renderFriends(list) {
	const panel = document.getElementById("friend-list-container");
	panel.innerHTML = list.map(f => 
	`<label class="friend-item">
	<input type="checkbox" name="friendUcodes" value="${f.ucode}">
	<span class="icon"><img src="${CTX}/img/manatoku.png" alt="profile"></span>
	<div class="info">
	<span class="fname">${f.name}</span><span class="fid">@${f.id}</span><br>
	</div>
	</label>`)
	.join("");
}