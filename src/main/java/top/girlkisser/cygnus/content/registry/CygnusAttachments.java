package top.girlkisser.cygnus.content.registry;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.girlkisser.cygnus.Cygnus;

public interface CygnusAttachments
{
	DeferredRegister<AttachmentType<?>> R = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Cygnus.MODID);

	static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> reg(String id, AttachmentType<T> attachmentType)
	{
		return R.register(id, () -> attachmentType);
	}

//	DeferredHolder<AttachmentType<?>, AttachmentType<GasChunkAttachment>> GAS = reg("gas", AttachmentType.builder(GasChunkAttachment::new)
//		.serialize(GasChunkAttachment.CODEC, GasChunkAttachment::isDirty)
//		.build());
}
