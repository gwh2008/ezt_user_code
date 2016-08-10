package com.eztcn.user.eztcn.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;

import com.eztcn.user.eztcn.bean.Contact;

public class PhoneContactUtil {
	/** 得到手机通讯录联系人信息 **/
	private static List<Contact> getPhoneContacts(Context mContext) {
		List<Contact> contectList = new ArrayList<Contact>();
		ContentResolver resolver = mContext.getContentResolver();
		// 得到名称
		String[] projection = new String[] { People._ID, People.NAME,
				People.NUMBER };
		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, projection,
				null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				int PHONES_DISPLAY_NAME_INDEX = phoneCursor
						.getColumnIndex(PhoneLookup.DISPLAY_NAME);
				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				// 取得联系人的ID索引值
				String contactId = phoneCursor.getString(phoneCursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				// 得到 电话
				projection = new String[] { Contacts.Phones.PERSON_ID,
						Contacts.Phones.NUMBER };
				Cursor phone = resolver.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						projection,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = ?", new String[] { contactId }, null);// 第一个参数是确定查询电话号，第三个参数是查询具体某个人的过滤值
				// 一个人可能有几个号码
				while (phone.moveToNext()) {
					String phoneNumber = phone
							.getString(phone
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					Contact contact = new Contact();
					contact.setName(contactName);
					contact.setPhone(phoneNumber);
					contectList.add(contact);
				}
				phone.close();

			}

			phoneCursor.close();
		}
		return contectList;
	}
}
