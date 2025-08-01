create table products(
    id bigint auto_increment primary key,
    name varchar(255) not null,
    price decimal(10,2) not null,
    img_url varchar(512) not null
);

create table members(
    id bigint auto_increment primary key ,
    email varchar(255) not null unique ,
    password varchar(255) not null,
    role varchar(20) not null
);

create table wishes (
    id bigint auto_increment primary key,
    member_id bigint not null,
    product_id bigint not null,
    unique (member_id, product_id),
    foreign key (member_id) references members(id),
    foreign key (product_id) references products(id)
);

create table options (
    id bigint auto_increment primary key,
    name varchar(50) not null,
    quantity int not null,
    product_id bigint not null,
    unique (product_id, name),
    foreign key (product_id) references products(id) on delete cascade
);

create table orders (
    id bigint auto_increment primary key,
    option_id bigint not null,
    quantity int not null,
    order_date_time datetime not null,
    message varchar(500),

    foreign key (option_id) references options(id) on delete cascade
);