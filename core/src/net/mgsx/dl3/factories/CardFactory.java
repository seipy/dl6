package net.mgsx.dl3.factories;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.utils.ObjectMap;

import net.mgsx.dl3.model.Card;
import net.mgsx.dl3.model.CardCell;
import net.mgsx.dl3.model.Component;
import net.mgsx.dl3.model.Dirs;
import net.mgsx.dl3.model.components.Power;
import net.mgsx.dl3.model.components.PowerGND;

public class CardFactory {

	public static Card fromMap(TiledMap map){
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
		
		Card card = new Card(layer.getWidth(), layer.getHeight());
		
		ObjectMap<Integer, Integer> tileToDirs = Dirs.createTileToDirs();
		
		for(int y=0 ; y<card.h ; y++){
			for(int x=0 ; x<card.w ; x++){
				Cell cell = layer.getCell(x, y);
				CardCell cardCell = card.cell(x, y);
				Component component = null;
				if(cell != null && cell.getTile() != null){
					int tid = cell.getTile().getId() - 1;
					Integer dirs = tileToDirs.get(tid);
					if(dirs != null){
						cardCell.dirs = dirs;
						cardCell.conductor = true;
					}else{
						if(tid == 4){
							cardCell.dirs = Dirs.RIGHT;
							cardCell.entity = new Power();
							cardCell.conductor = true;
						}else if(tid == 68){
							cardCell.dirs = Dirs.LEFT;
							cardCell.entity = new PowerGND();
							cardCell.conductor = true;
						}
					}
				}
				card.cell(x,y).component = component;
			}
		}
		
		
		return card;
	}

	public static void setTile(TiledMap map, CardCell cell) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		TiledMapTileSet tileset = map.getTileSets().getTileSet(0);
		
		// TODO
		int id = cell.component.type.tileBaseID;
		
		if(cell.component != null){
			if(cell.component.type.vertical){
				id = 101;
			}else{
				id = 100;
			}
		}
		
		layer.getCell(cell.x, cell.y).setTile(tileset.getTile(id + 1));
		
	}
}