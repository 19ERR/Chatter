package com.chatter.classes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Conversation {
    private String key;
    private String name;
    private List<Contact> participants = new List<Contact>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<Contact> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return null;
        }

        @Override
        public boolean add(Contact contact) {
            return false;
        }

        @Override
        public boolean remove(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends Contact> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends Contact> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Contact get(int index) {
            return null;
        }

        @Override
        public Contact set(int index, Contact element) {
            return null;
        }

        @Override
        public void add(int index, Contact element) {

        }

        @Override
        public Contact remove(int index) {
            return null;
        }

        @Override
        public int indexOf(@Nullable Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(@Nullable Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<Contact> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<Contact> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<Contact> subList(int fromIndex, int toIndex) {
            return null;
        }
    };
    private List<Message> messages = new List<Message>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<Message> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return null;
        }

        @Override
        public boolean add(Message message) {
            return false;
        }

        @Override
        public boolean remove(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends Message> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends Message> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Message get(int index) {
            return null;
        }

        @Override
        public Message set(int index, Message element) {
            return null;
        }

        @Override
        public void add(int index, Message element) {

        }

        @Override
        public Message remove(int index) {
            return null;
        }

        @Override
        public int indexOf(@Nullable Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(@Nullable Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<Message> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<Message> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<Message> subList(int fromIndex, int toIndex) {
            return null;
        }
    };
}
