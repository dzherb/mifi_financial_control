package storage;

import java.io.*;

public class StoragesSerializer {
    final static String serializedObjectsPath = "src/main/resources/";

    final static Storage<?>[] storages = new Storage[]{
            UsersStorage.getInstance(),
            WalletsStorage.getInstance(),
            TransactionsStorage.getInstance(),
            TransactionCategoriesStorage.getInstance(),
            SpendingLimitsStorage.getInstance(),
    };

    public static void serialize() throws IOException, ClassNotFoundException {
        for (Storage<? extends Storable> storageInstance : storages) {
            FileOutputStream fos = new FileOutputStream(getFilePath(storageInstance));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(storageInstance);
            fos.close();
        }
    }

    public static void deserialize() throws IOException, ClassNotFoundException {
        for (Storage<? extends Storable> storageInstance : storages) {
            FileInputStream fis = new FileInputStream(getFilePath(storageInstance));
            ObjectInputStream ois = new ObjectInputStream(fis);
            Storage<? extends Storable> obj = (Storage<? extends Storable>) ois.readObject();
            storageInstance.updateInstance(obj);
            ois.close();
        }
    }

    private static String getFilePath(Object obj) {
        return serializedObjectsPath + obj.getClass().getSimpleName() + ".ser";
    }
}
