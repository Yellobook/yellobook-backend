import csv
import os
import random
import time
import traceback
from collections import defaultdict
from datetime import datetime

from faker import Faker

fake = Faker('ko_KR')

csv_files = [
    'members.csv', 'teams.csv', 'participants.csv', 'informs.csv',
    'orders.csv', 'inventories.csv', 'products.csv', 'order_mentions.csv',
    'inform_mentions.csv', 'order_comments.csv', 'inform_comments.csv'
]

# CSV
[
    members_file,
    teams_file,
    participants_file,
    informs_file,
    orders_file,
    inventories_file,
    products_file,
    order_mentions_file,
    inform_mentions_file,
    order_comments_file,
    inform_comments_file
] = csv_files

# 데이터 개수
NUM_USERS = 100000
NUM_TEAMS = 100
USERS_PER_TEAM = 10000
NUM_COMMENTS = 100000
NUM_INFORMS = 1000000
NUM_INVENTORIES_PER_TEAM = 5
NUM_PRODUCTS_PER_INVENTORY = 100
NUM_COMMENT_PER_COMMENTER = 2
NUM_ORDERS = 1000000

# enums
ORDER_STATUS = ['PENDING_CONFIRM', 'PENDING_MODIFY', 'CONFIRMED']

# 재사용 목적
members = []
teams = []
participants = []
inventories = []
products = []
orders = []

team_participants_map = defaultdict(list)

start_date = datetime(2024, 1, 1)
end_date = datetime(2024, 9, 30)


def get_csv_writer(file):
    return csv.writer(file, lineterminator='\n')


def create_members():
    with open(members_file, 'w', newline='', encoding='utf-8') as file:
        writer = get_csv_writer(file)
        writer.writerow(
            ['id', 'nickname', 'email', 'role', 'allowance', 'profile_image', 'created_at', 'updated_at', 'deleted_at'])

        for member_id in range(1, NUM_USERS + 1):
            nickname = fake.name()
            email = f"example{member_id}@gmail.com"
            role = 'USER'
            allowance = 1
            profile_image = fake.image_url()
            created_at = fake.date_time_between(start_date='-2y', end_date=start_date).strftime(
                '%Y-%m-%d %H:%M:%S').strip()
            updated_at = created_at
            deleted_at = r'\N'
            writer.writerow(
                [member_id, nickname, email, role, allowance, profile_image, created_at, updated_at, deleted_at])
            members.append({'id': member_id, 'nickname': nickname, 'role': role, 'created_at': created_at})


def create_teams():
    with open(teams_file, 'w', newline='', encoding='utf-8') as file:
        writer = get_csv_writer(file)
        writer.writerow(['id', 'name', 'phone_number', 'address', 'created_at', 'updated_at'])

        for i in range(1, NUM_TEAMS + 1):
            name = f'팀_{i}'
            phone_number = fake.phone_number()
            address = fake.address()
            created_at = fake.date_time_between(start_date=start_date, end_date=end_date).strftime(
                '%Y-%m-%d %H:%M:%S').strip()
            updated_at = created_at

            writer.writerow([i, name, phone_number, address, created_at, updated_at])
            teams.append({'id': i, 'name': name})


def create_participants():
    with open(participants_file, 'w', newline='', encoding='utf-8') as file:
        writer = get_csv_writer(file)
        writer.writerow(['id', 'member_id', 'team_id', 'role', 'created_at', 'updated_at'])

        participant_id = 1

        for team in teams:
            admin_assigned = False

            selected_members = defaultdict(int)
            member_count = 0
            while member_count < USERS_PER_TEAM:
                member = random.choice(members)
                member_id = member['id']

                if selected_members[member_id]:
                    continue

                selected_members[member_id] = 1
                member_count += 1

                created_at = member['created_at']
                updated_at = created_at

                if not admin_assigned:
                    role = 'ADMIN'
                    admin_assigned = True
                else:
                    role = random.choice(['ORDERER', 'VIEWER'])

                team_id = team['id']

                writer.writerow([participant_id, member_id, team['id'], role, created_at, updated_at])

                participants.append({'team_id': team['id'], 'member_id': member_id, 'role': role})
                team_participants_map[team_id].append({'member_id': member_id, 'role': role})
                participant_id += 1


def create_inventories_and_products():
    with open(inventories_file, 'w', newline='', encoding='utf-8') as inv_file, open(products_file, 'w', newline='',
                                                                                     encoding='utf-8') as pd_file:
        inv_writer = get_csv_writer(inv_file)
        inv_writer.writerow(['id', 'team_id', 'title', 'view', 'created_at', 'updated_at'])

        prod_writer = get_csv_writer(pd_file)
        prod_writer.writerow(
            ['id', 'inventory_id', 'name', 'sub_product', 'sku', 'amount', 'purchase_price', 'sale_price', 'created_at',
             'updated_at'])

        inventory_id = 1
        product_id = 1

        for team in teams:
            team_id = team['id']

            for _ in range(NUM_INVENTORIES_PER_TEAM):
                title = f"재고_{team_id}-{inventory_id}"
                view = random.randint(0, 100)
                created_at = fake.date_time_between(start_date='-1y', end_date='now').strftime(
                    '%Y-%m-%d %H:%M:%S').strip()
                updated_at = created_at

                inventories.append({'id': inventory_id, 'team_id': team_id})
                inv_writer.writerow([inventory_id, team_id, title, view, created_at, updated_at])

                for _ in range(NUM_PRODUCTS_PER_INVENTORY):
                    name = fake.name()
                    sub_product = fake.name()
                    sku = product_id
                    amount = random.randint(1, 10000)
                    purchase_price = random.randint(10000, 1000000)
                    sale_price = purchase_price + random.randint(purchase_price, purchase_price + 1000)
                    prod_writer.writerow(
                        [product_id, inventory_id, name, sub_product, sku, amount, purchase_price, sale_price,
                         created_at, updated_at])
                    products.append({'id': product_id, 'inventory_id': inventory_id, 'amount': amount})
                    product_id += 1

                inventory_id += 1


def create_inform_with_mentions_and_comments():
    with open(informs_file, 'w', newline='', encoding='utf-8') as inform_file, \
            open(inform_mentions_file, 'w', newline='', encoding='utf-8') as mention_file, \
            open(inform_comments_file, 'w', newline='', encoding='utf-8') as comment_file:
        inform_writer = get_csv_writer(inform_file)
        mention_writer = get_csv_writer(mention_file)
        comment_writer = get_csv_writer(comment_file)

        inform_writer.writerow(
            ['id', 'member_id', 'team_id', 'title', 'content', 'date', 'view', 'created_at', 'updated_at'])
        mention_writer.writerow(['id', 'inform_id', 'member_id', 'created_at', 'updated_at'])
        comment_writer.writerow(['id', 'inform_id', 'member_id', 'content', 'created_at', 'updated_at'])

        mention_id = 1
        comment_id = 1

        for inform_id in range(1, NUM_INFORMS + 1):

            team_id = random.choice(list(team_participants_map.keys()))
            team_participants = team_participants_map[team_id]
            participant = random.choice(team_participants)
            member_id = participant['member_id']

            title = fake.catch_phrase()
            content = fake.catch_phrase()
            date = fake.date_between(start_date=start_date, end_date=end_date)
            view = random.randint(0, 100)
            created_at = fake.date_time_between(start_date=date, end_date=end_date).strftime(
                '%Y-%m-%d %H:%M:%S').strip()
            updated_at = created_at
            inform_writer.writerow([inform_id, member_id, team_id, title, content, date, view, created_at, updated_at])

            if team_participants:

                mentioned_people_count = random.randint(0, min(len(team_participants), 10))
                mentioned_people = random.sample(team_participants, mentioned_people_count)
                for mp in mentioned_people:
                    if mp['member_id'] != member_id:
                        mention_writer.writerow([mention_id, inform_id, mp['member_id'], created_at, updated_at])
                        mention_id += 1

                commenter_count = random.randint(0, len(mentioned_people))
                commenters = random.sample(mentioned_people, commenter_count)
                for c in commenters:
                    comment_writer.writerow(
                        [comment_id, inform_id, c['member_id'], fake.catch_phrase(), created_at,
                         updated_at])
                    comment_id += 1


def create_order_with_mentions_and_comments():
    with open(orders_file, 'w', newline='', encoding='utf-8') as order_file, \
            open(order_mentions_file, 'w', newline='', encoding='utf-8') as mention_file, \
            open(order_comments_file, 'w', newline='', encoding='utf-8') as comment_file:

        order_writer = get_csv_writer(order_file)
        mention_writer = get_csv_writer(mention_file)
        comment_writer = get_csv_writer(comment_file)

        order_writer.writerow(
            ['id', 'member_id', 'product_id', 'team_id', 'date', 'order_amount', 'view', 'memo',
             'order_status', 'created_at', 'updated_at'])
        mention_writer.writerow(['id', 'member_id', 'order_id', 'created_at', 'updated_at'])
        comment_writer.writerow(['id', 'member_id', 'order_id', 'content', 'created_at', 'updated_at'])

        mention_id = 1
        comment_id = 1

        orderers = [p for p in participants if p['role'] == 'ORDERER']

        team_inventory_ids_map = defaultdict(list)
        for i in inventories:
            team_inventory_ids_map[i['team_id']].append(i['id'])

        inventory_product_map = defaultdict(list)
        for pi, p in enumerate(products):
            inventory_product_map[p['inventory_id']].append({'products_idx': pi, 'amount': p['amount'], 'id': p['id']})

        for order_id in range(1, NUM_ORDERS + 1):

            participant = random.choice(orderers)
            team_id = participant['team_id']
            member_id = participant['member_id']

            inventory_id = random.choice(team_inventory_ids_map[team_id])
            available_products = [p for p in inventory_product_map[inventory_id] if p['amount'] > 0]
            if len(available_products) == 0:
                continue

            product = random.choice(available_products)
            product_id = product['id']
            date = fake.date_between(start_date=start_date, end_date=end_date)
            order_amount = random.randint(1, product['amount'])
            view = random.randint(0, 100)
            memo = fake.catch_phrase()
            order_status = random.choice(ORDER_STATUS)
            created_at = fake.date_time_between(start_date=date, end_date=end_date).strftime(
                '%Y-%m-%d %H:%M:%S').strip()
            updated_at = created_at

            products[product['products_idx']]['amount'] -= order_amount

            order_writer.writerow(
                [order_id, member_id, product_id, team_id, date, order_amount, view, memo, order_status,
                 created_at, updated_at])

            admin = next(p for p in team_participants_map[team_id] if p['role'] == 'ADMIN')
            admin_id = admin['member_id']

            # 각 팀의 관리자 언급
            mention_writer.writerow(
                [mention_id, admin_id, order_id, created_at, updated_at]
            )
            mention_id += 1

            # 관리자
            comment_writer.writerow(
                [comment_id, member_id, order_id, fake.catch_phrase(), created_at, updated_at])
            comment_id += 1

            comment_writer.writerow(
                [comment_id, admin_id, order_id, fake.catch_phrase(), created_at, updated_at])
            comment_id += 1


def measure_execution_time(func):
    start_time = time.time()
    func()
    end_time = time.time()
    print(f"{func.__name__} 실행 시간: {end_time - start_time:.2f}초")


def cleanup_csv_files():
    for file in csv_files:
        try:
            os.remove(file)
            print(f"{file} 삭제 완료")
        except FileNotFoundError:
            pass


def start(funcs):
    try:
        cleanup_csv_files()
        for func in funcs:
            measure_execution_time(func)
    except Exception as e:
        print(f"에러 발생: {e}")
        traceback.print_exc()


start([
    create_members,
    # create_teams,
    # create_participants,
    # create_inventories_and_products,
    # create_order_with_mentions_and_comments,
    # create_inform_with_mentions_and_comments
])
