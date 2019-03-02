package Evil_Code_DropHeads;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;

public class ItemDropListener implements Listener{
	DropHeads pl;
	ItemDropListener(){
		pl = DropHeads.getPlugin();
	}

	@SuppressWarnings("deprecation") @EventHandler
	public void onBarf(ItemSpawnEvent evt){
		if(Utils.isPlayerHead(evt.getEntity().getItemStack().getType())){
			ItemStack skullItem = evt.getEntity().getItemStack();
			if(skullItem.hasItemMeta() && skullItem.getItemMeta() instanceof SkullMeta){
				SkullMeta meta = (SkullMeta) skullItem.getItemMeta();

				GameProfile profile = Utils.getGameProfile(meta);
				if(profile != null){
					int idx = profile.getName().indexOf("|");
					if(idx != -1){
						EntityType type = EntityType.valueOf(profile.getName().substring(0, idx));
						evt.getEntity().setItemStack(Utils.makeTextureSkull(type, profile.getName().substring(idx+1)));
					}
					else{
						OfflinePlayer p;
						if(profile.getId() != null && (p = pl.getServer().getOfflinePlayer(profile.getId())) != null
								&& !p.getName().equals(profile.getName())){
							evt.getEntity().setItemStack(Utils.getPlayerHead(p.getUniqueId(), p.getName()));
						}
						else{
							meta.setOwner(profile.getName());
							meta.setDisplayName(ChatColor.WHITE+profile.getName()
									+ (profile.getName().startsWith("MHF_") ? "" : " Head"));
							skullItem.setItemMeta(meta);
							evt.getEntity().setItemStack(skullItem);
						}
					}
				}//if(profile != null)
			}//if(skull has meta)
		}//if(drop == player_skull)
	}//evt
}