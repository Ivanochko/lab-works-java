package genericVol2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Dispatcher {

	public static void main(String[] args) {

		ArrayList<Animal> arrayListAnimals = Creator.createAnimalsForArrayList();
		Animal[] staticArrayAnimals = Creator.createAnimalsForStaticArray();
		HashSet<Animal> hashSetAnimals = Creator.createAnimalsForSet();

		HashMap<Animal, Integer> hashResult = Controller.getHashMap(arrayListAnimals, staticArrayAnimals,
				hashSetAnimals);

		System.out.println("Всі елементи:");
		Controller.viewAll(arrayListAnimals, staticArrayAnimals, hashSetAnimals);

		System.out.println("\n-----------------------------------");
		System.out.println("Елементи, що дублюються:");
		Controller.viewResult(hashResult);
		System.out.println("-----------------------------------");
		
		System.out.println("Всі елементи після видалення:");
		staticArrayAnimals = Controller.removeDublicats(arrayListAnimals, staticArrayAnimals, hashSetAnimals);

		Controller.viewAll(arrayListAnimals, staticArrayAnimals, hashSetAnimals);

	}
}

class Controller {

	public static <T extends Animal> HashMap<Animal, Integer> getHashMap(ArrayList<? extends Animal> alAnimals,
			T[] arrayAnimals, HashSet<? extends Animal> hsAnimals) {
		HashMap<Animal, Integer> result = new HashMap<>();

		for (Animal animalFromAL : alAnimals) {
			for (Animal animalFromHS : hsAnimals) {
				if (animalFromAL.equals(animalFromHS)) {
					if (result.containsKey(animalFromAL)) {
						result.put(animalFromAL, result.get(animalFromAL) + 1);
					} else {
						result.put(animalFromAL, 2);
					}
				}
			}

		}
		for (Animal animalFromAL : alAnimals) {
			for (T t : arrayAnimals) {
				if (t.equals(animalFromAL)) {
					if (result.containsKey(t)) {
						result.put(t, result.get(t) + 1);
					} else {
						result.put(t, 2);
					}
				}
			}
		}
		return result;
	}

	public static void viewResult(HashMap<Animal, Integer> hashMapAnimals) {
		for (Map.Entry<Animal, Integer> out : hashMapAnimals.entrySet()) {
			System.out.println(out.getKey().name + " " + out.getValue());
		}
	}

	public static <T extends Animal> Animal[] removeDublicats(ArrayList<? extends Animal> alAnimals, T[] arrayAnimals,
			HashSet<? extends Animal> hsAnimals) {
		ArrayList<Animal> tempList = new ArrayList<>(Arrays.asList(arrayAnimals));
		ArrayList<Animal> dublicates = new ArrayList<>();

		for (Animal animalFromAL : alAnimals) {
			for (Animal animalFromHS : hsAnimals) {
				for (Animal animalFromArray : tempList) {
					if (animalFromAL.equals(animalFromHS) || animalFromAL.equals(animalFromArray)) {
						dublicates.add(animalFromAL);
					} else if (animalFromHS.equals(animalFromArray)) {
						dublicates.add(animalFromHS);
					}
				}
			}
		}
		for (Animal animal : dublicates) {
			alAnimals.remove(animal);
			tempList.remove(animal);
			hsAnimals.remove(animal);
		}

		Animal[] arrayAnimalsToReturn = new Animal[tempList.size()];

		int index = 0;
		for (Animal animal : tempList) {
			arrayAnimalsToReturn[index++] = animal;
		}
		
		return arrayAnimalsToReturn;
	}

	public static <T extends Animal, V extends Animal, M extends Animal> void viewAll(ArrayList<T> alAnimals,
			V[] arrayAnimals, HashSet<M> hsAnimals) {
		System.out.println("\nArrayList: ");
		alAnimals.forEach(elem -> System.out.println(elem.name));

		System.out.println("\nArray: ");
		for (V elem : arrayAnimals) {
			System.out.println(elem.name);
		}

		System.out.println("\nHashSet: ");
		hsAnimals.forEach(elem -> System.out.println(elem.name));

	}
}

class Creator {
	public static ArrayList<Animal> createAnimalsForArrayList() {
		ArrayList<Animal> result = new ArrayList<>();

		Dog dog1 = new Dog("homely", "Jack", 10);
		Dog dog2 = new Dog("homely", "Sam", 5);
		Dog dog3 = new Dog("guide", "Rocky", 3);
		result.add(dog1);
		result.add(dog2);
		result.add(dog3);

		Cat cat1 = new Cat("sofa", "Oscar", 3);
		Cat cat2 = new Cat("rug", "Smudge", 2);
		Cat cat3 = new Cat("furnace", "Alfie", 1);
		result.add(cat1);
		result.add(cat2);
		result.add(cat3);

		Poodle poodle1 = new Poodle("homely", "Murphy", 1);
		Poodle poodle2 = new Poodle("model", "Oliver", 2);
		result.add(poodle1);
		result.add(poodle2);

		Shepherd shepherd1 = new Shepherd("police", "Ollie", 6);
		Shepherd shepherd2 = new Shepherd("guard", "Ziggy", 4);
		result.add(shepherd1);
		result.add(shepherd2);

		return result;
	}

	public static Animal[] createAnimalsForStaticArray() {
		Dog dog4 = new Dog("watcher", "Duke", 4);
		Dog dog5 = new Dog("homely", "Sam", 5);

		Cat cat4 = new Cat("rug", "Jasper", 2);

		Poodle poodle3 = new Poodle("nurse", "Toby", 5);

//		Shepherd shepherd4 = new Shepherd("police", "Oreo", 8);
		Shepherd shepherd4 = new Shepherd("guard", "Ziggy", 4);
		Shepherd shepherd5 = new Shepherd("guard", "Ziggy", 4);

		Animal[] result = { dog4, dog5, cat4, poodle3, shepherd4, shepherd5 };

		return result;
	}

	public static HashSet<Animal> createAnimalsForSet() {

		HashSet<Animal> result = new HashSet<>();

		Dog dog6 = new Dog("homely", "Sam", 5);

		Cat cat5 = new Cat("sofa", "Oscar", 3);

		Poodle poodle4 = new Poodle("model", "Oliver", 2);

		Shepherd shepherd3 = new Shepherd("guard", "Ziggy", 4);
		Shepherd shepherd9 = new Shepherd("guard", "Ivan", 4);

		result.add(dog6);
		result.add(cat5);
		result.add(poodle4);
		result.add(shepherd3);
		result.add(shepherd9);

		return result;
	}
}

abstract class Animal {
	String name;
	int age;

	Animal(String name, int age) {
		this.name = name;
		this.age = age;
	}

	Animal() {
	}

	@Override
	public String toString() {
		return "" + this.name + ", " + this.age + " years";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Animal temp = (Animal) obj;
		return this.name.equals(temp.name) && this.age == temp.age;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode() + 17 * this.age;
	}

	abstract public void say();

}

class Dog extends Animal {
	String specialty;

	Dog(String specialty, String name, int age) {
		super(name, age);
		this.specialty = specialty;
	}

	Dog() {
	}

	@Override
	public String toString() {
		return super.toString() + ", has a " + this.specialty + " specialty";
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && this.specialty.equals(((Dog) (obj)).specialty);
	}

	@Override
	public int hashCode() {
		return super.hashCode() + this.specialty.hashCode();
	}

	@Override
	public void say() {
		System.out.println(this.name + ": Woof!");
	}
}

class Cat extends Animal {
	String favoritePlaceSleep;

	Cat(String favoritePlaceSleep, String name, int age) {
		super(name, age);
		this.favoritePlaceSleep = favoritePlaceSleep;
	}

	Cat() {
	}

	@Override
	public String toString() {
		return super.toString() + ", " + this.favoritePlaceSleep + " place to sleep";
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && this.favoritePlaceSleep.equals(((Cat) (obj)).favoritePlaceSleep);
	}

	@Override
	public int hashCode() {
		return super.hashCode() + this.favoritePlaceSleep.hashCode();
	}

	@Override
	public void say() {
		System.out.println(this.name + ": Meow!");
	}
}

class Poodle extends Dog {
	Poodle(String specialty, String name, int age) {
		super(specialty, name, age);
	}

	Poodle() {
	}

	@Override
	public String toString() {
		return "Poodle " + super.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.specialty.hashCode();
	}

	@Override
	public void say() {
		System.out.print("Poodle ");
		super.say();
	}
}

class Shepherd extends Dog {
	Shepherd(String specialty, String name, int age) {
		super(specialty, name, age);
	}

	Shepherd() {
	}

	@Override
	public String toString() {
		return "Shepherd " + super.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.specialty.hashCode();
	}

	@Override
	public void say() {
		System.out.print("Shepherd ");
		super.say();
	}
}