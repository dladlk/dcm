package dk.erst.cm.api.item;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dk.erst.cm.api.dao.mongo.ItemRepository;
import dk.erst.cm.api.data.Item;
import dk.erst.cm.api.load.model.Catalogue;
import dk.erst.cm.api.load.model.CatalogueLine;
import dk.erst.cm.api.load.model.Party;
import dk.erst.cm.api.load.model.SchemeID;

@Service
public class ItemService {

	private ItemRepository itemRepository;

	@Autowired
	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public Item saveCatalogUpdateItem(Catalogue catalogue, CatalogueLine line) {
		String lineLogicalId = line.getLogicalId();
		String sellerLogicalId = buildSellerLocalId(catalogue);

		String itemLogicalId = sellerLogicalId + "_" + lineLogicalId;

		Item item;
		Optional<Item> optional = itemRepository.findById(itemLogicalId);
		if (optional.isPresent()) {
			item = optional.get();
			item.setUpdateTime(Instant.now());
			item.setVersion(item.getVersion() + 1);
			item.setDocumentVersion(ItemDocumentVersion.PEPPOL_1_0);
			item.setDocument(line);
		} else {
			item = new Item();
			item.setId(itemLogicalId);
			item.setCreateTime(Instant.now());
			item.setUpdateTime(null);
			item.setVersion(1);
			item.setDocumentVersion(ItemDocumentVersion.PEPPOL_1_0);
			item.setDocument(line);
		}
		itemRepository.save(item);
		return item;
	}

	private String buildSellerLocalId(Catalogue catalogue) {
		if (catalogue.getSellerSupplierParty() != null) {
			Party sellerParty = catalogue.getSellerSupplierParty().getParty();
			if (sellerParty.getPartyIdentification() != null) {
				SchemeID schemeID = sellerParty.getPartyIdentification().getId();
				if (schemeID != null) {
					return schemeID.getLogicalId();
				}
			}
			if (sellerParty.getEndpointID() != null) {
				return sellerParty.getEndpointID().getLogicalId();
			}
		}
		return null;
	}

	public long countItems() {
		return itemRepository.count();
	}

	public List<Item> findAll() {
		return itemRepository.findAll();
	}

	public Optional<Item> findById(String id) {
		return itemRepository.findById(id);
	}
}
